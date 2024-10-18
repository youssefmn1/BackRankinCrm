package ma.emsi.minicrm.security;


import ma.emsi.minicrm.dao.entities.User;
import ma.emsi.minicrm.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encode the password
        userRepository.save(user);  // Save the user to the database
    }

}
