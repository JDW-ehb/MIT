package ehb.be.mit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserAccountController {
    @GetMapping("/login")
    public String login() {
        return "login"; // loads login.html from templates dir
    }

}
