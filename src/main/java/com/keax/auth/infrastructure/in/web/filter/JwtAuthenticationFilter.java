package com.keax.auth.infrastructure.in.web.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import com.keax.auth.domain.ports.out.TokenVerifierPort;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.FilterChain;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenVerifierPort tokenVerifierPort;

    public JwtAuthenticationFilter(
            UserDetailsService userDetailsService,
            TokenVerifierPort tokenVerifierPort
    ) {
        this.userDetailsService = userDetailsService;
        this.tokenVerifierPort = tokenVerifierPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = authHeader.substring(7);

            try {
                var subject = tokenVerifierPort.extractSubject(token);
                if (subject.isPresent()) {
                    String username = subject.get();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (UsernameNotFoundException ex) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

}
