package ehb.be.mit.controllers;

import ehb.be.mit.repositories.ObjectiveRepository;
import ehb.be.mit.services.ObjectiveService;
import ehb.be.mit.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/objectives")
public class ObjectiveController {

    private final ObjectiveService objectives;
    private final UserService users;
    private final ObjectiveRepository objectiveRepository;

    public ObjectiveController(ObjectiveService objectives,
                               UserService users, ObjectiveRepository objectiveRepository) {
        this.objectives = objectives;
        this.users = users;
        this.objectiveRepository = objectiveRepository;
    }

    @GetMapping("/create")
    public String createForm(Model model, Principal principal) {

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        long todayCount =
                objectives.countTodayForUser(user.getId());

        boolean canCreate = todayCount < 3;

        model.addAttribute("canCreate", canCreate);
        model.addAttribute("remaining", 3 - todayCount);

        if (todayCount >= 3) {
            return "redirect:/catalog?limitReached";
        }

        model.addAttribute("remaining", 3 - todayCount);
        return "creation";
    }

    @PostMapping("/create")
    public String submit(
            @RequestParam List<String> titles,
            @RequestParam List<String> descriptions,
            @RequestParam List<LocalDate> deadlines,
            Principal principal,
            Model model
    ) {

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        long todayCount = objectives.countTodayForUser(user.getId());
        long remaining = 3 - todayCount;

        if (remaining <= 0) {
            return "redirect:/catalog?limitReached";
        }

        int created = 0;

        for (int i = 0; i < titles.size(); i++) {

            if (titles.get(i) == null || titles.get(i).isBlank())
                continue;

            if (created >= remaining)
                break;

            objectives.createObjectiveForUser(
                    user.getId(),
                    titles.get(i).trim(),
                    descriptions.get(i),
                    deadlines.get(i)
            );

            created++;
        }

        return "redirect:/catalog";
    }

    @GetMapping("/{id}/edit")
    public String editStack(
            @PathVariable UUID id,
            Principal principal,
            Model model
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var clicked = objectives.getById(id);

        // Security sanity: ensure it belongs to user
        if(!clicked.getUsers().contains(user)){
            throw new RuntimeException("Access denied");
        }

        var date = clicked.getCreatedAt();

        var allThatDay =
                objectives.getAllForUserAndDate(user.getId(), date);

        model.addAttribute("date", date);
        model.addAttribute("objectives", allThatDay);

        return "edit";
    }

    @GetMapping("/{id}/view")
    public String viewDay(
            @PathVariable UUID id,
            Model model
    ){
        var clicked = objectives.getById(id);


        var owner = clicked.getUsers().stream().findFirst()
                .orElseThrow();

        var date = clicked.getCreatedAt();

        var allThatDay =
                objectives.getAllForUserAndDate(owner.getId(), date);

        model.addAttribute("date", date);
        model.addAttribute("username", owner.getUsername());
        model.addAttribute("objectives", allThatDay);

        return "view";
    }

    @GetMapping("/edit")
    public String editToday(Principal principal){

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        // Get today's date
        var today = LocalDate.now();

        // Fetch ALL objectives for today
        var todaysObjectives =
                objectives.getAllForUserAndDate(user.getId(), today);

        if(todaysObjectives.isEmpty()){
            return "redirect:/catalog?noObjectives";
        }

        // Just grab one id — your edit page loads the whole day anyway
        var first = todaysObjectives.get(0);

        return "redirect:/objectives/" + first.getId() + "/edit";
    }






    @PostMapping("/update")
    public String update(
            @RequestParam List<UUID> ids,
            @RequestParam List<String> descriptions,
            @RequestParam List<LocalDate> deadlines,
            Principal principal
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        for(int i = 0; i < ids.size(); i++){

            var obj = objectives.getById(ids.get(i));

            if(!obj.getUsers().contains(user))
                continue;

            obj.setDescription(descriptions.get(i));
            obj.setDeadline(deadlines.get(i));

            objectives.save(obj);
        }

        return "redirect:/catalog";
    }





}