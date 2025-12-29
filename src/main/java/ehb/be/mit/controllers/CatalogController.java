package ehb.be.mit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class CatalogController {

    @GetMapping("/catalog")
    public String catalog(Model model) {

        var users = List.of(
                Map.of(
                        "username", "Neo",
                        "objectives", List.of("Learn Spring Boot", "Defeat procrastination", "Go to gym")
                ),
                Map.of(
                        "username", "Trinity",
                        "objectives", List.of("Hack the Matrix", "Drink coffee")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                ),
                Map.of(
                        "username", "Morpheus",
                        "objectives", List.of("Find The One", "Teach Kung Fu", "Meditate")
                )
        );

        model.addAttribute("users", users);

        var today = LocalDateTime.now();
        var formatted = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // fake user id for demo
        String userId = "demo-user-123";

        // pretend this is your service later
        boolean hasCreatedToday = false;   // ← flip to false to test

        model.addAttribute("hasCreatedToday", hasCreatedToday);

        model.addAttribute("todayText", "Today's objectives on: " + formatted);

        return "catalog";
    }
}
