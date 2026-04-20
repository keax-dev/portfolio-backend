package com.keax.auth.infrastructure.out.security;

import org.springframework.beans.factory.annotation.Value;
import com.keax.auth.domain.ports.out.TokenProviderPort;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil implements TokenProviderPort {

    @Value("${jwt.secret}")
    private String key;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(String username) {

        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSignInKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

}
