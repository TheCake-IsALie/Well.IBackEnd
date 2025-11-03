package org.generation.wellibackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                                .requestMatchers(
                                        "/api/users/login",
                                        "/api/users/register"
                                ).permitAll()
                                .requestMatchers("/api/news/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/meteo/**").permitAll()
                                .requestMatchers("/uploads/**").permitAll()
                                .requestMatchers("/api/events/**").authenticated()
                                .requestMatchers("/api/spotify/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/users/upload-avatar").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/users/update").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/users/delete").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/users/userinformation").authenticated()
                                .requestMatchers("/api/gemini/**").authenticated()
                                .anyRequest().authenticated()

                ).addFilterBefore(filtro, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder getCypher()
    {
        return new BCryptPasswordEncoder();
    }
}