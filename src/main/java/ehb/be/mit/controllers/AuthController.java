package ehb.be.mit.controllers;

import ehb.be.mit.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // loads login.html from templates dir
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String country,
                                 Model model) {

        try {
            users.register(username, email, password, country);
            return "redirect:/login";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
