package org.generation.wellibackend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder getCypher() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // SPECIFICA L'ORIGINE DEL TUO FRONTEND (es. Angular su 4200)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // Specifica i metodi HTTP permessi (GET, POST, ecc.)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Specifica gli header che il frontend può inviare (importante per l'autenticazione)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // Permette al browser di inviare cookie/header di autenticazione (es. JWT)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Applica questa configurazione a TUTTI gli endpoint ("/**")
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                // Gestisce gli errori di autenticazione per le API
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Se la richiesta fallita è per l'API (non autenticata)
                            if (request.getRequestURI().startsWith("/api/")) {
                                // Invia un errore 401 (Unauthorized) invece di reindirizzare
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non autorizzato");
                            } else {
                                // Altrimenti (es. un utente cerca di accedere a /profilo)
                                response.sendRedirect("/login");
                            }
                        })
                )

                // Configurazione delle rotte
                .authorizeHttpRequests(auth -> auth

                        // 1. Permetti a tutti di accedere all'API qui sotto (per testare)
                        .requestMatchers("/api/events/**").permitAll()
                        .requestMatchers("/mood/**").permitAll()
                        .requestMatchers("/api/news/**").permitAll()

                        //---------------------------------------------------

                        // 2. Lascia le altre rotte API solo per ADMIN
                        .requestMatchers("/api/**").hasRole("ADMIN")

                        // 3. Permetti a tutti di accedere a /public, /login e alla pagina /error
                        .requestMatchers("/public/**", "/login", "/error", "/register").permitAll()

                        // 4. Tutte le altre richieste (es. /profilo) richiedono autenticazione
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                );

        return http.build();
    }
}