package ch.wisv.events.core.service.order;

import ch.wisv.events.core.exception.OrderNotFound;
import ch.wisv.events.core.exception.ProductLimitExceededException;
import ch.wisv.events.core.model.order.Customer;
import ch.wisv.events.core.model.order.Order;
import ch.wisv.events.core.model.order.OrderStatus;
import ch.wisv.events.core.model.product.Product;
import ch.wisv.events.core.repository.OrderRepository;
import ch.wisv.events.core.service.product.ProductService;
import ch.wisv.events.core.service.product.SoldProductService;
import ch.wisv.events.app.request.SalesOrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2016  W.I.S.V. 'Christiaan Huygens'
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
@Service
public class OrderServiceImpl implements OrderService {

    /**
     * Field orderRepository
     */
    private final OrderRepository orderRepository;

    /**
     * Field productService
     */
    private final ProductService productService;


    /**
     * Field soldProductService
     */
    private final SoldProductService soldProductService;

    /**
     * Constructor OrderServiceImpl creates a new OrderServiceImpl instance.
     *
     * @param orderRepository    of type OrderRepository
     * @param productService     of type ProductService
     * @param soldProductService
     */
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService,
                            SoldProductService soldProductService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.soldProductService = soldProductService;
    }

    /**
     * Method getAllOrders returns the allOrders of this OrderService object.
     *
     * @return the allOrders (type List<Order>) of this OrderService object.
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream().filter(x -> x.getCustomer() != null).collect(Collectors.toList());
    }

    /**
     * Method getByReference returns Order with the given Reference.
     *
     * @param reference of type String
     * @return Order
     */
    @Override
    public Order getByReference(String reference) {
        Optional<Order> orderOption = orderRepository.findByPublicReference(reference);
        if (orderOption.isPresent()) {
            return orderOption.get();
        }
        throw new OrderNotFound("Order with reference " + reference + " not found!");
    }

    /**
     * Method getOrdersByProduct returns list of orders with a certain product in it.
     *
     * @param product of type Product
     * @return List<Order>
     */
    @Override
    public List<Order> getOrdersByProduct(Product product) {
        List<Order> orders = this.getAllOrders();
        return orders.stream().filter(x -> x.getProducts().stream().anyMatch(p -> p == product))
                     .collect(Collectors.toList());
    }

    /**
     * Method create creates a new order by SalesOrderRequest.
     *
     * @param orderRequest of type SalesOrderRequest
     * @return Order
     */
    @Override
    public Order create(SalesOrderRequest orderRequest) {
        Order order = new Order();
        for (Map.Entry<String, Integer> entry : orderRequest.getProducts().entrySet()) {
            Product product = productService.getByKey(entry.getKey());

            if (product.getSold() + entry.getValue() > product.getMaxSold()) {
                throw new ProductLimitExceededException(
                        "Not enough products of " + product.getTitle() + ", only " + (product
                                .getMaxSold() - product.getSold()) + " items left. ");
            }

            for (Integer i = 0; i < entry.getValue(); i++) {
                order.addProduct(product);
            }
        }

        // TODO: check iff there are products in the order

        orderRepository.saveAndFlush(order);

        return order;
    }

    /**
     * Method addCustomerToOrder will add a customer to an order.
     *
     * @param order    of type Order
     * @param customer of type Customer
     */
    @Override
    public void addCustomerToOrder(Order order, Customer customer) {
        order.setCustomer(customer);

        orderRepository.save(order);
    }

    /**
     * Method updateOrderStatus will update the order status and update the product count.
     *
     * @param order       of type Order
     * @param orderStatus of type OrderStatus
     */
    @Override
    public void updateOrderStatus(Order order, OrderStatus orderStatus) {
        OrderStatus old = order.getStatus();
        if (!old.toString().contains("PAID") && orderStatus.toString().contains("PAID")) {
            order.getProducts().forEach(x -> x.setSold(x.getSold() + 1));
            soldProductService.create(order);
        } else if (old.toString().contains("PAID") && !orderStatus.toString().contains("PAID")) {
            order.getProducts().forEach(x -> x.setSold(x.getSold() - 1));
            soldProductService.remove(order);
        }

        order.setStatus(orderStatus);
        orderRepository.save(order);
    }
}
