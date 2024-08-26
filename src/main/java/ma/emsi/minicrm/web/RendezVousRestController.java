package ma.emsi.minicrm.web;

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

        // Traitement de la création du rendez-vous
        rendezVous.setLead(leadService.getLeadById(rendezVous.getLead().getId()));
        rendezVous.setCommercial(commercialService.getCommercialById(rendezVous.getCommercial().getId()));
        RendezVous createdRendezVous = rendezVousService.createRendezVous(rendezVous);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRendezVous);
    }



    // Update an existing rendezvous (PUT /api/v1/rendezvous/{id})
    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> updateRendezVous(@PathVariable Integer id, @RequestBody RendezVous rendezVous) {
        if (!id.equals(rendezVous.getId())) {
            return ResponseEntity.badRequest().build();
        }
        RendezVous updatedRendezVous = rendezVousService.updateRendezVous(id, rendezVous);
        return ResponseEntity.ok(updatedRendezVous);
    }

    // Delete a rendezvous by its ID (DELETE /api/v1/rendezvous/{id})
    @DeleteMapping("/{id}")
            public ResponseEntity<Void> deleteRendezVous(@PathVariable Integer id) {
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }
}
