package ch.wisv.events.app.controller.dashboard;

import ch.wisv.events.core.exception.OrderNotFound;
import ch.wisv.events.core.model.order.Order;
import ch.wisv.events.core.service.order.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
@Controller
@RequestMapping(value = "/dashboard/orders")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardOrderController {

    /**
     * OrderService.
     */
    private final OrderService orderService;

    /**
     * Default constructor
     *
     * @param orderService OrderService
     */
    public DashboardOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());

        return "dashboard/orders/index";
    }

    @GetMapping("/edit/{key}/")
    public String edit(Model model, @PathVariable String key) {
        try {
            Order order = orderService.getByReference(key);
            model.addAttribute("order", order);

            return "dashboard/orders/edit";
        } catch (OrderNotFound e) {
            return "redirect:/dashboard/orders/";
        }
    }

}
