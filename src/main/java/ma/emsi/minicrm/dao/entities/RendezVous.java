package ma.emsi.minicrm.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.TIME)
    private Time heure;

    private String lieu;

    @OneToOne
    @JoinColumn(name = "lead_id")
    @JsonIgnoreProperties({"commercial", "rendezVous", "interactions", "otherFields"})
    private Lead lead;

    @ManyToOne
    @JoinColumn(name = "commercial_id")
    @JsonIgnoreProperties({"leads", "rendezVousList", "otherFields"})
    private Commercial commercial;
}
