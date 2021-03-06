package ch.wisv.events.app.controller.dashboard;

import ch.wisv.events.core.exception.ProductNotFound;
import ch.wisv.events.core.model.product.Product;
import ch.wisv.events.core.service.product.SoldProductService;
import ch.wisv.events.core.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * DashboardProductController.
 */
@Controller
@RequestMapping("/dashboard/products")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardProductController {

    /**
     * ProductService
     */
    private final ProductService productService;

    /**
     * Field orderService
     */
    private final SoldProductService soldProductService;

    /**
     * Default constructor
     *
     * @param productService     ProductService
     * @param soldProductService SoldProductService
     */
    @Autowired
    public DashboardProductController(ProductService productService, SoldProductService soldProductService) {
        this.productService = productService;
        this.soldProductService = soldProductService;
    }

    /**
     * Get request for ProductOverview
     *
     * @param model SpringUI model
     * @return thymeleaf template path
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productService.getAllProducts());

        return "dashboard/products/index";
    }

    /**
     * Get request to create a Product
     *
     * @param model SpringUI model
     * @return thymeleaf template path
     */
    @GetMapping("/create/")
    public String create(Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new Product());
        }

        return "dashboard/products/create";
    }

    /**
     * Get request to edit a Product or if the key does not exists it will redirect to the
     * Product Overview page
     *
     * @param model SpringUI model
     * @return thymeleaf template path
     */
    @GetMapping("/edit/{key}/")
    public String edit(Model model, @PathVariable String key) {
        try {
            Product product = productService.getByKey(key);
            model.addAttribute("product", product);

            return "dashboard/products/edit";
        } catch (ProductNotFound e) {
            return "redirect:/dashboard/products/";
        }
    }

    /**
     * Method overview will show a list of the users with this product.
     *
     * @param model of type Model
     * @param key   of type String
     * @return String
     */
    @GetMapping("/overview/{key}/")
    public String overview(Model model, @PathVariable String key) {
        try {
            Product product = productService.getByKey(key);

            model.addAttribute("soldProducts", soldProductService.getByProduct(product));
            model.addAttribute("product", product);

            return "dashboard/products/overview";
        } catch (ProductNotFound e) {
            return "redirect:/dashboard/products/";
        }
    }

    /**
     * Post request to add a Product
     *
     * @param model    Product model attr.
     * @param redirect Spring RedirectAttributes
     * @return redirect
     */
    @PostMapping("/add")
    public String add(RedirectAttributes redirect, @ModelAttribute Product model) {
        try {
            productService.add(model);
            redirect.addFlashAttribute("message", "Product " + model.getTitle() + " has been successfully created!");

            return "redirect:/dashboard/products/";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            redirect.addFlashAttribute("product", model);

            return "redirect:/dashboard/products/create/";
        }
    }


    /**
     * Method edit post request to update an existing Product
     *
     * @param redirect of type RedirectAttributes
     * @param model    of type Product
     * @return String
     */
    @PostMapping("/update")
    public String update(RedirectAttributes redirect, @ModelAttribute Product model) {
        try {
            productService.update(model);
            redirect.addFlashAttribute("message", "Changes have been saved!");
        } catch (ProductNotFound e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard/products/edit/" + model.getKey() + "/";
    }

    /**
     * Get request to delete a Product
     *
     * @param redirectAttributes Spring RedirectAttributes
     * @param key                key of a Product
     * @return redirect
     */
    @GetMapping("/delete/{key}")
    public String delete(RedirectAttributes redirectAttributes, @PathVariable String key) {
        Product product = productService.getByKey(key);
        try {
            productService.delete(product);
            redirectAttributes.addFlashAttribute("message", "Product " + product.getTitle() + " has been deleted!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard/products/";
    }

}
