package ma.emsi.minicrm.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LeadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lead_id", nullable = false)
    @JsonIgnoreProperties({"interactions", "commercial", "rendezVous", "files"})
    private Lead lead;

    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String telephone;
    private String source;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    private String note;

    @Column(name = "modification_type", nullable = false)
    private String modificationType;  // e.g., "CREATE", "UPDATE", "DELETE"

    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}
