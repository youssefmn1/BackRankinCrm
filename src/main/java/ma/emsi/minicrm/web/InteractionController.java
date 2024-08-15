package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Interaction;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.services.InteractionService;
import ma.emsi.minicrm.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/interactions")
public class InteractionController {
    @Autowired
    private InteractionService interactionService;
    @Autowired
    private LeadService leadService;
    @GetMapping
    public String getAllInteractions(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String keyword) {
        Page<Interaction> interactionPage = interactionService.findPaginated(page, size, keyword);
        List<Lead> leads = leadService.findAll();
        model.addAttribute("interactions", interactionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", interactionPage.getTotalPages());
        return "interactions";
    }

    @GetMapping("/{id}")
    public String getInteractionById(@PathVariable Integer id, Model model) {
        Interaction interaction = interactionService.getInteractionById(id);
        if (interaction != null) {
            model.addAttribute("interaction", interaction);
            return "interactions";
        } else {
            return "interactions";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("interaction", new Interaction());
        model.addAttribute("leads", leadService.findAll()); // Add this line
        return "create-interactions";
    }


    @PostMapping
    public String createInteraction(@ModelAttribute Interaction interaction) {
        interactionService.saveInteraction(interaction);
        return "redirect:/interactions";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Interaction interaction = interactionService.getInteractionById(id);
        List<Lead> leads = leadService.findAll(); // Fetch the list of leads
        if (interaction != null) {
            model.addAttribute("interaction", interaction);
            model.addAttribute("leads", leads); // Add the list of leads to the model
            return "edit-interaction";
        } else {
            return "error404"; // Adjust error handling as needed
        }
    }


    @PostMapping("/{id}")
    public String updateInteraction(@PathVariable Integer id, @ModelAttribute Interaction interaction) {
        interaction.setId(id);
        interactionService.saveInteraction(interaction);
        return "redirect:/interactions";
    }

    @PostMapping("/{id}/delete")
    public String deleteInteraction(@PathVariable Integer id) {
        interactionService.deleteInteraction(id);
        return "redirect:/interactions";
    }

}
