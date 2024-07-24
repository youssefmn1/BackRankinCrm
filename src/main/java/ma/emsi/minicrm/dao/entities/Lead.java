package ma.emsi.minicrm.dao.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
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
    private String note;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interaction> interactions;

    @ManyToOne
    @JoinColumn(name = "commercial_id")
    private Commercial commercial;


    @OneToOne(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    private RendezVous rendezVous;
}