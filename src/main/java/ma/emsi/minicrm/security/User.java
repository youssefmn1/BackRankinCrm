package ma.emsi.minicrm.security;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.util.Set;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ElementCollection
    private Set<String> roles;

    // Getters and setters
}
