package org.generation.wellibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").hasRole("ADMIN")    // solo admin
                        .requestMatchers("/public/**").permitAll()      // endpoint pubblici
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())                     // nuova sintassi per httpBasic
                .formLogin(form -> form    // nuova sintassi per formLogin
                        .loginPage("/login")                            // puoi personalizzare se serve
                        .permitAll()
                );
        return http.build();
    }
}
