package ma.emsi.minicrm.security;

import ma.emsi.minicrm.dao.entities.LoginRequest;
import ma.emsi.minicrm.dao.entities.LoginResponse;
import ma.emsi.minicrm.dao.entities.User;
import ma.emsi.minicrm.dao.entities.Utilisateur;
import ma.emsi.minicrm.dao.repositories.UtilisateurRepository;
import ma.emsi.minicrm.security.JwtTokenProvider;
import ma.emsi.minicrm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        userService.registerUser(user);  // Appel du service pour enregistrer l'utilisateur
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            String token = jwtTokenProvider.generateToken(authentication);

            Utilisateur utilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            return ResponseEntity.ok(new LoginResponse(token, utilisateur.getId(), utilisateur.getEmail()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
