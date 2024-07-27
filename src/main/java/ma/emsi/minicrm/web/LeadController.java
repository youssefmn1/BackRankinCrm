package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    // Show the form to create a new lead
    @GetMapping("/new")
    public String showCreateLeadForm(Model model) {
        model.addAttribute("lead", new Lead());
        return "create-lead";  // Returns the name of the HTML template
    }

    // Create a new lead
    @PostMapping
    public String createLead(@ModelAttribute Lead lead) {
        lead.setRendezVous(null);
        lead.setCommercial(null);
        leadService.createLead(lead);
        return "redirect:/leads";  // Redirect to the list of leads
    }


    // Retrieve a lead by its ID
    @GetMapping("/{id}")
    public String getLeadById(@PathVariable Integer id, Model model) {
        Lead lead = leadService.getLeadById(id);
        model.addAttribute("lead", lead);
        return "lead-details";  // Returns the name of the HTML template
    }

    // Retrieve all leads
    @GetMapping
    public String getAllLeads(Model model) {
        List<Lead> leads = leadService.getAllLeads();
        model.addAttribute("leads", leads);
        return "leads";  // Returns the name of the HTML template
    }

    // Show the form to update an existing lead
    @GetMapping("/edit/{id}")
    public String showUpdateLeadForm(@PathVariable Integer id, Model model) {
        Lead lead = leadService.getLeadById(id);
        model.addAttribute("lead", lead);
        return "edit-lead";  // Returns the name of the HTML template
    }

    // Update an existing lead
    @PostMapping("/edit/{id}")
    public String updateLead(@PathVariable Integer id, @ModelAttribute Lead leadDetails) {
        leadService.updateLead(id, leadDetails);
        return "redirect:/leads";  // Redirect to the list of leads
    }

    // Delete a lead by its ID
    @GetMapping("/delete/{id}")
    public String deleteLead(@PathVariable Integer id) {
        leadService.deleteLead(id);
        return "redirect:/leads";  // Redirect to the list of leads
    }
}
