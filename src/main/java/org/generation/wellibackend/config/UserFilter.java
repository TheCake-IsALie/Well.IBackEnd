package org.generation.wellibackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils; // <-- 1. IMPORTA QUESTO
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserFilter extends OncePerRequestFilter {

    @Autowired
    UserService uService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = "";


        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Estrae il token dopo "Bearer "
        }

        if (token.isEmpty()) {
            Cookie[] cookies = request.getCookies();
            if(cookies!=null) {
                for(Cookie c : cookies) {
                    if(c.getName().equals("token")) {
                        token = c.getValue();
                    }
                }
            }
        }



        if(!token.isEmpty()) {
            try {
                User u = uService.findUserByToken(token);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                u,
                                null,
                                u.getAuthorities()
                        )
                );
            } catch (Exception e) {
                System.err.println("Token non valido: " + token);
            }

        }
        filterChain.doFilter(request, response);
    }
}