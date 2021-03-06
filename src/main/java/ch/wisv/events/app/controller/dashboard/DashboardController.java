package ch.wisv.events.app.controller.dashboard;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DashboardController.
 */
@Controller
@RequestMapping(value = "/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    /**
     * Default constructor
     */
    public DashboardController() {
    }

    /**
     * Get request on "/dashboard/" will show index.
     *
     * @param model SpringUI Model
     * @return path to Thymeleaf template
     */
    @GetMapping("/")
    public String index(Model model) {
        return "dashboard/index";
    }

}
