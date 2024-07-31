package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.entities.Role;
import ma.emsi.minicrm.services.CommercialService;
import ma.emsi.minicrm.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/commercials")
public class CommercialController {

    @Autowired
    private CommercialService commercialService;
    @Autowired
    private LeadService leadService;

    // Show the form to create a new commercial
    @GetMapping("/new")
    public String showCreateCommercialForm(Model model) {
        model.addAttribute("commercial", new Commercial());
        return "create-commercial";  // Returns the name of the HTML template
    }

    // Create a new commercial
    @PostMapping
    public String createCommercial(@ModelAttribute Commercial commercial) {
        commercialService.createCommercial(commercial);
        commercial.setRole(Role.Commercial);
        return "redirect:/commercials";  // Redirect to the list of commercials
    }

    // Retrieve a commercial by its ID
    @GetMapping("/{id}")
    public String getCommercialById(@PathVariable Integer id, Model model) {
        Commercial commercial = commercialService.getCommercialById(id);
        model.addAttribute("commercial", commercial);
        return "commercial-details";  // Returns the name of the HTML template
    }

    // Retrieve all commercials
    @GetMapping
    public String getAllCommercials(Model model,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "10") int size,
                                    @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<Commercial> pageCommercials = commercialService.findPaginated(page, size, kw);
        model.addAttribute("commercials", pageCommercials.getContent());
        model.addAttribute("pages", new int[pageCommercials.getTotalPages()]); // collect an array with a size of number of pages
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "commercials";  // Returns the name of the HTML template
    }

    // Show the form to update an existing commercial
    @GetMapping("/edit/{id}")
    public String showUpdateCommercialForm(@PathVariable Integer id, Model model) {
        Commercial commercial = commercialService.getCommercialById(id);
        model.addAttribute("commercial", commercial);
        return "edit-commercial";  // Returns the name of the HTML template
    }

    // Update an existing commercial
    @PostMapping("/edit/{id}")
    public String updateCommercial(@PathVariable Integer id, @ModelAttribute Commercial commercialDetails) {
        commercialService.updateCommercial(id, commercialDetails);
        return "redirect:/commercials";  // Redirect to the list of commercials
    }

    // Delete a commercial by its ID
    @GetMapping("/delete/{id}")
    public String deleteCommercial(@PathVariable Integer id) {
        commercialService.deleteCommercial(id);
        return "redirect:/commercials";  // Redirect to the list of commercials
    }

    // Delete selected commercials
    @PostMapping("/deleteSelected")
    public String deleteSelectedCommercials(@RequestParam("selectedCommercials") List<Integer> selectedCommercialIds) {
        for (Integer commercialId : selectedCommercialIds) {
            commercialService.deleteCommercial(commercialId);
        }
        return "redirect:/commercials";  // Redirect to the list of commercials
    }
}
