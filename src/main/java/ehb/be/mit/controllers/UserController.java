package ehb.be.mit.controllers;

import ehb.be.mit.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService users;

    public UserController(UserService users){
        this.users = users;
    }

    @GetMapping("/me")
    public String myProfile(Principal principal){
        return "redirect:/users/" + principal.getName();
    }

    @GetMapping("/users/{username}")
    public String profile(
            @PathVariable String username,
            Principal principal,
            Model model
    ){

        var user = users.findByUsername(username)
                .orElseThrow();

        boolean isOwner =
                principal != null &&
                        principal.getName().equals(username);

        model.addAttribute("user", user);
        model.addAttribute("isOwner", isOwner);

        return "profile";
    }
}
