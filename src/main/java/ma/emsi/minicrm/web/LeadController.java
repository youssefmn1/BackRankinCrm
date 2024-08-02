package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.repositories.CommercialRepository;
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
    @Autowired
    private CommercialRepository commercialRepository;

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
    public String getAllLeads(Model model,
                              @RequestParam(name = "page",defaultValue = "0") int page,
                              @RequestParam(name = "size",defaultValue = "10") int size,
                              @RequestParam(name = "keyword",defaultValue = "") String kw) {
        Page<Lead> Pageleads = leadService.findPaginated(page,size,kw);
        List<Commercial> commercials = commercialRepository.findAll();
        model.addAttribute("leads", Pageleads.getContent());
        model.addAttribute("pages",new int[Pageleads.getTotalPages()]);// collecter un tableau avec une size de nombre de page
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",kw);
        model.addAttribute("commercials", commercials);
        return "leads";  // Returns the name of the HTML template
    }

    // Show the form to update an existing lead
    @GetMapping("/edit/{id}")
    public String showUpdateLeadForm(@PathVariable Integer id, Model model) {
        Lead lead = leadService.getLeadById(id);
        List<Commercial> commercials = commercialRepository.findAll();
        model.addAttribute("lead", lead);
        model.addAttribute("commercials", commercials);
        return "edit-lead";
    }

    @PostMapping("/edit/{id}")
    public String updateLead(@PathVariable Integer id, @ModelAttribute Lead leadDetails) {
        Lead existingLead = leadService.getLeadById(id);
        if (leadDetails.getCommercial() != null) {
            Commercial commercial = commercialRepository.findById(leadDetails.getCommercial().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid commercial Id:" + leadDetails.getCommercial().getId()));
            existingLead.setCommercial(commercial);
        } else {
            existingLead.setCommercial(null); // or handle it as you need
        }
        existingLead.setNom(leadDetails.getNom());
        existingLead.setPrenom(leadDetails.getPrenom());
        existingLead.setEmail(leadDetails.getEmail());
        existingLead.setAdresse(leadDetails.getAdresse());
        existingLead.setTelephone(leadDetails.getTelephone());
        existingLead.setSource(leadDetails.getSource());
        existingLead.setStatut(leadDetails.getStatut());
        existingLead.setNote(leadDetails.getNote());
        leadService.updateLead(id, existingLead);
        return "redirect:/leads";
    }
    // Méthode pour afficher les leads par l'ID du commercial
    @GetMapping("/commercial/{commercialId}")
    public String getLeadsByCommercialId(@PathVariable Integer commercialId, Model model) {
        List<Lead> leads = leadService.getLeadsByCommercialId(commercialId);
        model.addAttribute("leads", leads);
        return "leads";  // Nom de votre template HTML
    }

    // Delete a lead by its ID
    @GetMapping("/delete/{id}")
    public String deleteLead(@PathVariable Integer id) {
        leadService.deleteLead(id);
        return "redirect:/leads";  // Redirect to the list of leads
    }
    // Delete selected leads
    @PostMapping("/deleteSelected")
    public String deleteSelectedLeads(@RequestParam("selectedLeads") List<Integer> selectedLeadIds) {
        for (Integer leadId : selectedLeadIds) {
            leadService.deleteLead(leadId);
        }
        return "redirect:/leads";  // Redirect to the list of leads
    }
}
