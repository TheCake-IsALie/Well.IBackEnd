package org.generation.wellibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder getCypher() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disattiva CSRF se non usi form HTML
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // tutte le richieste accessibili
                )
                .formLogin(form -> form.disable()) // disattiva login form
                .httpBasic(basic -> basic.disable()); // disattiva autenticazione base (popup browser)

        return http.build();
    }
}