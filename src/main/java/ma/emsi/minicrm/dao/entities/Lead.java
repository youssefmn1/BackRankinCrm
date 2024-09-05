package ma.emsi.minicrm.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String telephone;
    private String source;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    private String note;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"lead", "otherFields"})
    private List<Interaction> interactions;

    @ManyToOne
    @JoinColumn(name = "commercial_id")
    @JsonIgnoreProperties({"leads", "rendezVousList","files", "otherFields"})
    private Commercial commercial;

    @OneToOne(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"lead","commercial","files", "otherFields"})
    private RendezVous rendezVous;

    @OneToMany(mappedBy = "lead")
    @JsonIgnoreProperties({"lead","commercial","commercial", "otherFields"})
    private List<FileMetadata> files;
}
