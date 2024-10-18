package ma.emsi.minicrm.security;

import ma.emsi.minicrm.dao.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    // Méthode personnalisée pour trouver un utilisateur par email
    Optional<Utilisateur> findByEmail(String email);

    // Vous pouvez ajouter d'autres méthodes de recherche ici si nécessaire
}
