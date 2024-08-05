package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.RendezVous;
import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Lead;
import ma.emsi.minicrm.services.CommercialService;
import ma.emsi.minicrm.services.LeadService;
import ma.emsi.minicrm.services.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private LeadService leadService;

    @Autowired
    private CommercialService commercialService;

    // Show the form to create a new rendezvous
    @GetMapping("/new")
    public String showCreateRendezVousForm(Model model) {
        List<Lead> leads = leadService.getAllLeads(); // Get all leads
        List<Commercial> commercials = commercialService.getAllCommercials(); // Get all commercials

        model.addAttribute("leads", leads);
        model.addAttribute("commercials", commercials);
        model.addAttribute("rendezVous", new RendezVous());
        return "create-rendezvous";  // Returns the name of the HTML template
    }
    @PostMapping
    public String createRendezVous(@RequestParam("date") String date,
                                   @RequestParam("heure") String heure,
                                   @RequestParam("lieu") String lieu,
                                   @RequestParam("lead") Integer leadId,
                                   @RequestParam("commercial") Integer commercialId) {

        try {
            // Validation du format de l'heure (HH:mm:ss)
            java.sql.Time heureSQL = java.sql.Time.valueOf(heure + ":00");
            java.sql.Date dateSQL = java.sql.Date.valueOf(date);

            RendezVous rendezVous = new RendezVous();
            rendezVous.setDate(dateSQL);
            rendezVous.setHeure(heureSQL);
            rendezVous.setLieu(lieu);
            rendezVous.setLead(leadService.getLeadById(leadId));
            rendezVous.setCommercial(commercialService.getCommercialById(commercialId));

            rendezVousService.createRendezVous(rendezVous);
        } catch (IllegalArgumentException e) {
            // Gérer les erreurs de conversion ici
            e.printStackTrace();
            return "redirect:/rendezvous/new?error=true"; // Rediriger avec un indicateur d'erreur
        }

        return "redirect:/rendezvous";  // Rediriger vers la liste des rendezvous
    }


    // Retrieve a rendezvous by its ID
    @GetMapping("/{id}")
    public String getRendezVousById(@PathVariable Integer id, Model model) {
        RendezVous rendezVous = rendezVousService.getRendezVousById(id);
        model.addAttribute("rendezVous", rendezVous);
        return "rendezvous-details";  // Returns the name of the HTML template
    }

    // Retrieve all rendezvous
    @GetMapping
    public String getAllRendezVous(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<RendezVous> pageRendezVous = rendezVousService.findPaginated(page, size, kw);
        model.addAttribute("rendezvousList", pageRendezVous.getContent());
        model.addAttribute("pages", new int[pageRendezVous.getTotalPages()]); // collect an array with a size of number of pages
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        return "rendezvous";  // Returns the name of the HTML template
    }

    // Show the form to update an existing rendezvous
    @GetMapping("/edit/{id}")
    public String showUpdateRendezVousForm(@PathVariable Integer id, Model model) {
        RendezVous rendezVous = rendezVousService.getRendezVousById(id);
        List<Lead> leads = leadService.getAllLeads(); // Get all leads
        List<Commercial> commercials = commercialService.getAllCommercials(); // Get all commercials

        model.addAttribute("leads", leads);
        model.addAttribute("commercials", commercials);
        model.addAttribute("rendezVous", rendezVous);
        return "edit-rendezvous";  // Returns the name of the HTML template
    }

    // Update an existing rendezvous
    @PostMapping("/edit/{id}")
    public String updateRendezVous(@PathVariable Integer id, @RequestParam("date") String date,
                                   @RequestParam("heure") String heure,
                                   @RequestParam("lieu") String lieu,
                                   @RequestParam("lead") Integer leadId,
                                   @RequestParam("commercial") Integer commercialId) {

        RendezVous rendezVousDetails = new RendezVous();
        rendezVousDetails.setDate(java.sql.Date.valueOf(date));
        rendezVousDetails.setHeure(java.sql.Time.valueOf(heure));
        rendezVousDetails.setLieu(lieu);
        rendezVousDetails.setLead(leadService.getLeadById(leadId));
        rendezVousDetails.setCommercial(commercialService.getCommercialById(commercialId));

        rendezVousService.updateRendezVous(id, rendezVousDetails);
        return "redirect:/rendezvous";  // Redirect to the list of rendezvous
    }

    // Delete a rendezvous by its ID
    @GetMapping("/delete/{id}")
    public String deleteRendezVous(@PathVariable Integer id) {
        rendezVousService.deleteRendezVous(id);
        return "redirect:/rendezvous";  // Redirect to the list of rendezvous
    }

    // Delete selected rendezvous
    @PostMapping("/deleteSelected")
    public String deleteSelectedRendezVous(@RequestParam("selectedRendezVous") List<Integer> selectedRendezVousIds) {
        for (Integer rendezVousId : selectedRendezVousIds) {
            rendezVousService.deleteRendezVous(rendezVousId);
        }
        return "redirect:/rendezvous";  // Redirect to the list of rendezvous
    }
}
