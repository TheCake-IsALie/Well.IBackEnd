package org.generation.wellibackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserFilter filtro;


    @Bean
    public SecurityFilterChain sicurezza(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth->

                        auth
                        .requestMatchers("api/mood").permitAll()
                                .requestMatchers("api/users/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()

                ).addFilterBefore(filtro, UsernamePasswordAuthenticationFilter.class);//dove viene fatto, sempre uguale

        return http.build();
    }

    //creo bean del criptatore da autowirare
    @Bean
    public PasswordEncoder getCypher()
    {
        return new BCryptPasswordEncoder();
    }
}
