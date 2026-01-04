package ehb.be.mit.config;

import ehb.be.mit.services.ObjectiveService;
import ehb.be.mit.services.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalModel {

    private final ObjectiveService objectives;
    private final UserService users;

    public GlobalModel(ObjectiveService objectives, UserService users) {
        this.objectives = objectives;
        this.users = users;
    }

    @ModelAttribute("hasCreatedToday")
    public Boolean hasCreatedToday(Principal principal){

        if(principal == null)
            return false;

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        return objectives.countTodayForUser(user.getId()) > 0;
    }
}
