package ehb.be.mit.controllers;

import ehb.be.mit.models.Group;
import ehb.be.mit.repositories.IGroupRepository;
import ehb.be.mit.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private final IGroupRepository groups;
    private final UserService users;

    public GroupsController(IGroupRepository groups, UserService users){
        this.groups = groups;
        this.users = users;
    }

    @GetMapping
    public String groupCatalog(Model model){
        model.addAttribute("groups", groups.findAll());
        return "groups/groups";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("group", new Group());
        return "groups/creategroup";
    }

    @PostMapping("/create")
    public String createGroup(
            @ModelAttribute Group group,
            Principal principal
    ){
        var owner = users.findByUsername(principal.getName())
                .orElseThrow();

        // set owner
        group.setOwner(owner);

        // save first so it has an ID
        groups.save(group);

        // owner automatically joins
        group.addMember(owner);
        groups.save(group);

        return "redirect:/groups/" + group.getId();
    }

    @GetMapping("/{id}")
    public String viewGroup(@PathVariable UUID id, Model model){
        var group = groups.findById(id).orElseThrow();
        model.addAttribute("group", group);
        model.addAttribute("objectives", group.getObjectives());
        return "groups/groupview";
    }

    @GetMapping("/{id}/edit")
    public String editGroup(
            @PathVariable UUID id,
            Principal principal,
            Model model
    ){
        var group = groups.findById(id).orElseThrow();

        // ensure logged in + owner
        if (principal == null || !group.getOwner().getUsername().equals(principal.getName())) {
            return "redirect:/groups/" + id;  // deny politely
        }

        model.addAttribute("group", group);
        return "groups/editgroup";
    }


    @PostMapping("/{id}/edit")
    public String updateGroup(
            @PathVariable UUID id,
            @ModelAttribute Group form,
            Principal principal
    ){
        var group = groups.findById(id).orElseThrow();

        if (principal == null || !group.getOwner().getUsername().equals(principal.getName())) {
            return "redirect:/groups/" + id;
        }

        // update editable fields only
        group.setName(form.getName());
        group.setShortDescription(form.getShortDescription());
        group.setDescription(form.getDescription());

        groups.save(group);

        return "redirect:/groups/" + id;
    }





}
