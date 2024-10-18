package ma.emsi.minicrm.security;

import lombok.Getter;
import lombok.Setter;

public class LoginResponse {
    private String token;
    private String type = "Bearer";
    @Setter
    @Getter
    private Integer userId; // Add user ID
    @Setter
    @Getter
    private String email; // Add user email

    // Constructor
    public LoginResponse(String token, Integer userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    // Getters and Setters
    // ... (existing getters and setters)

}