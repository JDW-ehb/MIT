package ehb.be.mit.controllers;

import ehb.be.mit.services.CatalogService;
import ehb.be.mit.services.ObjectiveService;
import ehb.be.mit.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class CatalogController {

    private final CatalogService catalog;
    private final UserService userService;
    private final ObjectiveService objectives;

    public CatalogController(CatalogService catalog, UserService userService, ObjectiveService objectives) {
        this.catalog = catalog;
        this.userService = userService;
        this.objectives = objectives;
    }

    @GetMapping("/catalog")
    public String catalog(Model model, Principal principal) {

        var users = catalog.loadUsersWithObjectives();
        model.addAttribute("users", users);

        var today = LocalDate.now();
        model.addAttribute("todayText", "Today's objectives on: " + today);

        var user = userService.findByUsername(principal.getName()).orElseThrow();

        long todayCount = objectives.countTodayMITsForUser(user.getId());

        boolean hasReachedDailyLimit = todayCount >= 3;

        model.addAttribute("hasReachedDailyLimit", hasReachedDailyLimit);
        model.addAttribute("remaining", Math.max(0, 3 - todayCount));
        model.addAttribute("categories", catalog.loadAllCategories());


        model.addAttribute("me", user.getUsername());

        return "MIT pages/catalog";
    }

}

