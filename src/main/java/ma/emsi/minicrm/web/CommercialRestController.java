package ma.emsi.minicrm.web;

import ma.emsi.minicrm.dao.entities.Commercial;
import ma.emsi.minicrm.dao.entities.Role;
import ma.emsi.minicrm.services.CommercialService;
import ma.emsi.minicrm.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commercials")
public class CommercialRestController {

    @Autowired
    private CommercialService commercialService;

    @Autowired
    private LeadService leadService;

    // Create a new commercial
    @PostMapping
    public ResponseEntity<Commercial> createCommercial(@RequestBody Commercial commercial) {
        commercial.setRole(Role.Commercial);
        Commercial createdCommercial = commercialService.createCommercial(commercial);
        return new ResponseEntity<>(createdCommercial, HttpStatus.CREATED);
    }

    // Retrieve a commercial by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Commercial> getCommercialById(@PathVariable Integer id) {
        Commercial commercial = commercialService.getCommercialById(id);
        if (commercial != null) {
            return new ResponseEntity<>(commercial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Retrieve all commercials (without pagination)
    @GetMapping
    public ResponseEntity<List<Commercial>> getAllCommercials(@RequestParam(name = "keyword", defaultValue = "") String kw) {
        List<Commercial> commercials = commercialService.getAllCommercials();
        return new ResponseEntity<>(commercials, HttpStatus.OK);
    }

    // Update an existing commercial
    @PutMapping("/{id}")
    public ResponseEntity<Commercial> updateCommercial(@PathVariable Integer id, @RequestBody Commercial commercialDetails) {
        Commercial updatedCommercial = commercialService.updateCommercial(id, commercialDetails);
        if (updatedCommercial != null) {
            return new ResponseEntity<>(updatedCommercial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a commercial by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommercial(@PathVariable Integer id) {
        if (commercialService.getCommercialById(id) != null) {
            commercialService.deleteCommercial(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete selected commercials
    @PostMapping("/deleteSelected")
    public ResponseEntity<Void> deleteSelectedCommercials(@RequestBody List<Integer> selectedCommercialIds) {
        for (Integer commercialId : selectedCommercialIds) {
            if (commercialService.getCommercialById(commercialId) != null) {
                commercialService.deleteCommercial(commercialId);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
