package ehb.be.mit.controllers;

import ehb.be.mit.models.*;
import ehb.be.mit.repositories.ICategoryRepository;
import ehb.be.mit.repositories.IGroupRepository;
import ehb.be.mit.repositories.IObjectiveRepository;
import ehb.be.mit.services.ObjectiveService;
import ehb.be.mit.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ehb.be.mit.services.CategoryService;


import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class ObjectiveController {

    private final ObjectiveService objectives;
    private final UserService users;
    private final IObjectiveRepository IObjectiveRepository;
    private final IGroupRepository groups;
    private final ICategoryRepository categories;
    private final CategoryService categoryService;


    public ObjectiveController(
            ObjectiveService objectives,
            UserService users,
            IObjectiveRepository IObjectiveRepository,
            IGroupRepository groups,
            ICategoryRepository categories,
            CategoryService categoryService
    ){
        this.objectives = objectives;
        this.users = users;
        this.IObjectiveRepository = IObjectiveRepository;
        this.groups = groups;
        this.categories = categories;
        this.categoryService = categoryService;
    }


    /* ==============================
              CREATE MITs
       ============================== */

    @GetMapping("/objectives/create")
    public String createForm(Model model, Principal principal) {

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var today = LocalDate.now();

        var todays = objectives.getAllForUserAndDate(user.getId(), today)
                .stream()
                .filter(o -> o.getType() == ObjectiveType.MIT)
                .toList();

        var slots = new ArrayList<>(todays);
        while (slots.size() < 3)
            slots.add(new Objective());

        model.addAttribute("slots", slots);
        model.addAttribute("created", todays.size());
        model.addAttribute("remaining", 3 - todays.size());
        model.addAttribute("allCategories", categoryService.findAll());

        return "MIT pages/creation";
    }


    @PostMapping("/objectives/create")
    public String submit(
            @RequestParam List<String> titles,
            @RequestParam List<String> descriptions,
            @RequestParam List<String> statuses,
            @RequestParam(required = false) List<UUID> categoryIds,
            @RequestParam(required = false) List<String> newCategories,
            Principal principal
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        long already = objectives.countTodayMITsForUser(user.getId());
        long remaining = 3 - already;

        int created = 0;

        for (int i = 0; i < titles.size(); i++) {

            var title = titles.get(i);

            if (title == null || title.isBlank())
                continue;

            if (created >= remaining)
                break;

            // ---------- resolve category ----------
            Category cat = categoryService.resolve(
                    (categoryIds != null && categoryIds.size() > i) ? categoryIds.get(i) : null,
                    (newCategories != null && newCategories.size() > i) ? newCategories.get(i) : null
            );


            // ---------- build MIT ----------
            var obj = new Objective();
            obj.setTitle(title.trim());
            obj.setDescription(descriptions.get(i));
            obj.setStatus(ObjectiveStatus.valueOf(statuses.get(i)));
            obj.setType(ObjectiveType.MIT);
            obj.setCategory(cat);

            // link user properly
            obj.addUser(user);

            IObjectiveRepository.save(obj);

            created++;
        }

        return "redirect:/catalog";
    }



    /* ==============================
               EDIT MIT STACK
       ============================== */

    @GetMapping("/objectives/{id}/edit")
    public String editStack(
            @PathVariable UUID id,
            Principal principal,
            Model model
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var clicked = objectives.getById(id);

        if (!clicked.getUsers().contains(user))
            throw new RuntimeException("Access denied");

        var date = clicked.getCreatedAt();

        var allThatDay = objectives.getAllForUserAndDate(user.getId(), date);

        var slots = new ArrayList<Objective>(allThatDay);
        while (slots.size() < 3)
            slots.add(new Objective());

        model.addAttribute("objectives", slots);
        model.addAttribute("date", date);
        model.addAttribute("allCategories", categoryService.findAll());

        return "MIT pages/edit";
    }



    @PostMapping("/objectives/update")
    public String update(
            @RequestParam List<String> ids,
            @RequestParam List<String> titles,
            @RequestParam List<String> descriptions,
            @RequestParam List<String> statuses,
            @RequestParam(required=false) List<UUID> categoryIds,
            @RequestParam(required=false) List<String> newCategories,
            Principal principal
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        for (int i = 0; i < ids.size(); i++) {

            var id    = ids.get(i);
            var title = titles.get(i);
            var desc  = descriptions.get(i);

            // skip totally empty card
            if ((title == null || title.isBlank()) &&
                    (desc  == null || desc.isBlank()))
                continue;

            // ---------- resolve category ----------
            Category cat = categoryService.resolve(
                    (categoryIds != null && categoryIds.size() > i) ? categoryIds.get(i) : null,
                    (newCategories != null && newCategories.size() > i) ? newCategories.get(i) : null
            );


            // ---------- CREATE NEW MIT ----------
            if (id == null || id.isBlank()) {

                var obj = new Objective();
                obj.setTitle(title.trim());
                obj.setDescription(desc);
                obj.setStatus(ObjectiveStatus.valueOf(statuses.get(i)));
                obj.setType(ObjectiveType.MIT);
                obj.setCategory(cat);

                obj.addUser(user);

                IObjectiveRepository.save(obj);

                continue;
            }

            // ---------- UPDATE EXISTING MIT ----------
            var obj = objectives.getById(UUID.fromString(id));

            if (!obj.getUsers().contains(user))
                continue; // safety

            obj.setTitle(title);
            obj.setDescription(desc);
            obj.setStatus(ObjectiveStatus.valueOf(statuses.get(i)));
            obj.setCategory(cat);

            IObjectiveRepository.save(obj);
        }

        return "redirect:/catalog";
    }




    /* ==============================
               VIEW MIT STACK
       ============================== */

    @GetMapping("/objectives/{id}/view")
    public String viewDay(
            @PathVariable UUID id,
            Model model
    ){
        var clicked = objectives.getById(id);

        var owner = clicked.getUsers()
                .stream().findFirst()
                .orElseThrow();

        var date = clicked.getCreatedAt();

        var allThatDay = objectives.getAllForUserAndDate(owner.getId(), date);

        model.addAttribute("date", date);
        model.addAttribute("username", owner.getUsername());
        model.addAttribute("objectives", allThatDay);

        return "MIT pages/view";
    }


    @GetMapping("/objectives/edit")
    public String editToday(Principal principal){

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var today = LocalDate.now();

        var todaysObjectives =
                objectives.getAllForUserAndDate(user.getId(), today);

        if (todaysObjectives.isEmpty())
            return "redirect:/catalog?noObjectives";

        return "redirect:/objectives/" + todaysObjectives.get(0).getId() + "/edit";
    }



    @GetMapping("/subobjectives")
    public String listSubObjectives(Model model, Principal principal) {

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var subs = IObjectiveRepository.findByTypeNot(ObjectiveType.MIT);

        model.addAttribute("subs", subs);
        model.addAttribute("currentUser", user);

        return "subobjectives/subobjectives";
    }



    @GetMapping("/subobjectives/create")
    public String showSubObjectiveForm(Model model, Principal principal){

        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var obj = new Objective();
        obj.setGroup(new Group());

        model.addAttribute("objective", obj);

        model.addAttribute(
                "myGroups",
                groups.findByMembersUsername(user.getUsername())
        );

        model.addAttribute("allCategories", categoryService.findAll());

        return "subobjectives/subcreate";
    }




    @PostMapping("/subobjectives/create")
    public String createSubObjective(
            @ModelAttribute Objective objective,
            @RequestParam(required = false) UUID groupId,
            Principal principal
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        // set type BEFORE save
        objective.setType(ObjectiveType.SUBOBJECTIVE);

        // set group if present
        if (groupId != null) {
            var group = groups.findById(groupId).orElseThrow();

            if (!group.getMembers().contains(user)) {
                throw new RuntimeException("Not a member of this group.");
            }

            objective.setGroup(group);
        }

        // FIRST SAVE OBJECTIVE
        var saved = IObjectiveRepository.save(objective);

        // THEN LINK USER
        saved.addUser(user);

        // SAVE AGAIN (or cascade)
        IObjectiveRepository.save(saved);

        return "redirect:/subobjectives";
    }



    @GetMapping("/subobjectives/{id}/edit")
    public String editSubObjective(
            @PathVariable UUID id,
            Principal principal,
            Model model
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var obj = objectives.getById(id);

        if (!obj.getUsers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        model.addAttribute("objective", obj);
        model.addAttribute("me", user.getUsername());

        model.addAttribute(
                "myGroups",
                groups.findByMembersUsername(user.getUsername())
        );

        model.addAttribute("allCategories", categoryService.findAll());

        return "subobjectives/subedit";
    }


    @PostMapping("/subobjectives/{id}/edit")
    public String updateSubObjective(
            @PathVariable UUID id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam LocalDate deadline,
            @RequestParam(required = false) UUID groupId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String newCategory,
            Principal principal
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var obj = objectives.getById(id);

        if (!obj.getUsers().contains(user)) {
            throw new RuntimeException("Access denied");
        }

        obj.setTitle(title);
        obj.setDescription(description);
        obj.setDeadline(deadline);

        // -------- group --------
        if (groupId != null) {
            var group = groups.findById(groupId).orElseThrow();

            if (!group.getMembers().contains(user)) {
                throw new RuntimeException("Not a member");
            }

            obj.setGroup(group);
        } else {
            obj.setGroup(null);
        }

        // -------- category --------
        Category cat = categoryService.resolve(categoryId, newCategory);



        obj.setCategory(cat);

        IObjectiveRepository.save(obj);

        return "redirect:/subobjectives";
    }

    @GetMapping("/subobjectives/{id}/view")
    public String viewSubObjective(
            @PathVariable UUID id,
            Principal principal,
            Model model
    ){
        var user = users.findByUsername(principal.getName())
                .orElseThrow();

        var obj = objectives.getById(id);

        model.addAttribute("objective", obj);
        model.addAttribute("me", user.getUsername());

        return "subobjectives/subview";
    }
}
