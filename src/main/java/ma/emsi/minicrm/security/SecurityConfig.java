package ma.emsi.minicrm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, UserDetailsService userDetailsService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.userDetailsService = userDetailsService;
    }
/*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configurer CORS
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour les API REST
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions pour les API
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll() // Autoriser l'accès public à l'authentification
                        .anyRequest().authenticated() // Protéger toutes les autres routes
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // Ajouter le filtre JWT

        return http.build();
    }*/
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/api/auth/signup").permitAll()
                            .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .httpBasic(withDefaults());

    return http.build();
}

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Configurer les règles CORS
        config.setAllowCredentials(true); // Permettre l'envoi des informations d'authentification
        config.addAllowedOriginPattern("*"); // Autoriser toutes les origines (vous pouvez restreindre ici si nécessaire)
        config.addAllowedHeader("*"); // Autoriser tous les headers (Authorization, Content-Type, etc.)
        config.addAllowedMethod("*"); // Autoriser toutes les méthodes HTTP (GET, POST, PUT, DELETE, etc.)

        // Appliquer cette configuration à toutes les routes de l'application
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }


    // Configurer CORS pour permettre l'accès depuis le frontend (par exemple, React)
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Autoriser toutes les origines (à restreindre si besoin)
        config.addAllowedHeader("*"); // Autoriser tous les headers
        config.addAllowedMethod("*"); // Autoriser toutes les méthodes HTTP (GET, POST, etc.)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
