package ma.emsi.minicrm.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String typeInteraction;

    private String details;

    @ManyToOne
    @JoinColumn(name = "historique_interaction_id")
    private HistoriqueInteraction historiqueInteraction;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;
}
