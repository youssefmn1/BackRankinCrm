package ma.emsi.minicrm.web;

import jakarta.persistence.EntityNotFoundException;
import ma.emsi.minicrm.dao.entities.RendezVous;
import ma.emsi.minicrm.services.CommercialService;
import ma.emsi.minicrm.services.LeadService;
import ma.emsi.minicrm.services.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousRestController {

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private LeadService leadService;

    @Autowired
    private CommercialService commercialService;

    // Get all rendezvous (GET /api/v1/rendezvous)
    @GetMapping
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        List<RendezVous> rendezVousList = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(rendezVousList);
    }

    // Get a rendezvous by its ID (GET /api/v1/rendezvous/{id})
    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getRendezVousById(@PathVariable Integer id) {
        RendezVous rendezVous = rendezVousService.getRendezVousById(id);
        if (rendezVous == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rendezVous);
    }

    @PostMapping
    public ResponseEntity<RendezVous> createRendezVous(@RequestBody RendezVous rendezVous) {
        if (rendezVous == null) {
            System.out.println("Request body is missing!");
            return ResponseEntity.badRequest().build();
        }

        if (rendezVous.getLead() == null || rendezVous.getLead().getId() == null) {
            System.out.println("Lead is missing or has no ID!");
            return ResponseEntity.badRequest().build();
        }

        if (rendezVous.getCommercial() == null || rendezVous.getCommercial().getId() == null) {
            System.out.println("Commercial is missing or has no ID!");
            return ResponseEntity.badRequest().build();
        }

        // Récupération des entités Lead et Commercial à partir de leurs IDs
        rendezVous.setLead(leadService.getLeadById(rendezVous.getLead().getId()));
        rendezVous.setCommercial(commercialService.getCommercialById(rendezVous.getCommercial().getId()));

        // Vérification si le Lead et le Commercial existent bien dans la base de données
        if (rendezVous.getLead() == null) {
            System.out.println("Lead not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (rendezVous.getCommercial() == null) {
            System.out.println("Commercial not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Création du rendez-vous
        RendezVous createdRendezVous = rendezVousService.createRendezVous(rendezVous);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRendezVous);
    }



    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> updateRendezVous(@PathVariable Integer id, @RequestBody RendezVous rendezVous) {
        if (rendezVous == null || rendezVous.getId() == null || !id.equals(rendezVous.getId())) {
            return ResponseEntity.badRequest().body(null);
        }

        RendezVous existingRendezVous = rendezVousService.getRendezVousById(id);
        if (existingRendezVous == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        existingRendezVous.setDate(rendezVous.getDate());
        existingRendezVous.setHeure(rendezVous.getHeure());
        existingRendezVous.setLieu(rendezVous.getLieu());

        if (rendezVous.getLead() != null) {
            existingRendezVous.setLead(leadService.getLeadById(rendezVous.getLead().getId()));
        } else {
            existingRendezVous.setLead(null);
        }

        if (rendezVous.getCommercial() != null) {
            existingRendezVous.setCommercial(commercialService.getCommercialById(rendezVous.getCommercial().getId()));
        } else {
            existingRendezVous.setCommercial(null);
        }

        RendezVous updatedRendezVous = rendezVousService.updateRendezVous(id, existingRendezVous);
        return ResponseEntity.ok(updatedRendezVous);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRendezVous(@PathVariable Integer id) {
        System.out.println("Deleting rendezvous with ID: " + id);
        if (rendezVousService.getRendezVousById(id) != null) {
            rendezVousService.deleteRendezVous(id);
            System.out.println("Rendezvous deleted successfully.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            System.out.println("Rendezvous with ID " + id + " not found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Get rendez-vous by lead ID
    @GetMapping("/lead/{leadId}")
    public ResponseEntity<RendezVous> getRendezVousByLeadId(@PathVariable Integer leadId) {
        List<RendezVous> rendezVousList = rendezVousService.getRendezVousByLeadId(leadId);
        if (rendezVousList.isEmpty()) {
            return ResponseEntity.ok(null); // No rendez-vous found
        }
        return ResponseEntity.ok(rendezVousList.get(0)); // Return first rendez-vous
    }




}
