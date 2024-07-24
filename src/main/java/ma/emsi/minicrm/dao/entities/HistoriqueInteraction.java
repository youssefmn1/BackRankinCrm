package ma.emsi.minicrm.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HistoriqueInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String typeInteraction;

    private String details;

    @OneToMany(mappedBy = "historiqueInteraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interaction> interactions;
}
