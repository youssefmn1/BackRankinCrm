package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.services.CommercialService;
import ma.emsi.minicrm.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private CommercialService commercialService;

    @GetMapping
    public String listLeads(Model model, @RequestParam(defaultValue = "") String keyword,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("leads", leadService.findPaginated(page, size, keyword));
        model.addAttribute("keyword", keyword);
        return "leads";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("lead", new Lead());
        model.addAttribute("commercials", commercialService.getAllCommercials());
        return "create-lead";
    }

    @PostMapping
    public String createLead(@ModelAttribute Lead lead) {
        leadService.createLead(lead);
        return "redirect:/leads";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Lead lead = leadService.getLeadById(id);
        model.addAttribute("lead", lead);
        model.addAttribute("commercials", commercialService.getAllCommercials());
        return "edit-lead";
    }

    @PostMapping("/edit/{id}")
    public String updateLead(@PathVariable Integer id, @ModelAttribute Lead lead) {
        leadService.updateLead(id, lead);
        return "redirect:/leads";
    }

    @GetMapping("/delete/{id}")
    public String deleteLead(@PathVariable Integer id) {
        leadService.deleteLead(id);
        return "redirect:/leads";
    }

    @PostMapping("/deleteSelected")
    public String deleteSelected(@RequestParam List<Integer> selectedLeads) {
        selectedLeads.forEach(leadService::deleteLead);
        return "redirect:/leads";
    }
}
