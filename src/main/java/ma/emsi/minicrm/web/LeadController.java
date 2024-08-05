package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.services.LeadService;
import ma.emsi.minicrm.services.CommercialService;  // Ensure you have a service to fetch commercials
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private CommercialService commercialService;  // Add this service to fetch commercials

    @GetMapping
    public String getAllLeads(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size,
                              @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<Lead> Pageleads = leadService.findPaginated(page, size, kw);
        List<Commercial> commercials = commercialService.findAll();
        model.addAttribute("leads", Pageleads.getContent());
        model.addAttribute("commercials", commercials);
        model.addAttribute("pages", new int[Pageleads.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "leads";
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Commercial> commercials = commercialService.findAll();
        model.addAttribute("lead", new Lead());
        model.addAttribute("commercials", commercials);  // Add commercials to the model
        return "create-lead";
    }

    @PostMapping
    public String createLead(@ModelAttribute Lead lead) {
        leadService.createLead(lead);
        return "redirect:/leads";
    }

    @GetMapping("/commercial/{commercialId}")
    public String getLeadsByCommercialId(
            @PathVariable Integer commercialId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<Lead> leadsPage = leadService.getLeadsByCommercialId(commercialId, keyword, PageRequest.of(page, 10));
        String commercialName = commercialService.getCommercialNameById(commercialId);

        model.addAttribute("leads", leadsPage.getContent());
        model.addAttribute("commercialId", commercialId);
        model.addAttribute("commercialName", commercialName);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", leadsPage.getTotalPages());
        return "leadsbycommercial";
    }

    
    @PostMapping("/assignCommercial")
    public String assignCommercial(@RequestParam Integer leadId, @RequestParam Integer commercialId) {
        leadService.assignCommercial(leadId, commercialId);
        return "redirect:/leads";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Lead lead = leadService.getLeadById(id);
        List<Commercial> commercials = commercialService.findAll();  // Fetch the list of commercials
        model.addAttribute("lead", lead);
        model.addAttribute("commercials", commercials);  // Add commercials to the model
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

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/deleteSelected")
    @ResponseBody
    public ResponseEntity<Void> deleteSelectedLeads(@RequestBody List<Integer> leadIds) {
        try {
            leadService.deleteLeads(leadIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
