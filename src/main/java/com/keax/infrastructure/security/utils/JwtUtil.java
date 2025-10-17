package com.keax.infrastructure.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String key;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(key.getBytes());
    }

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
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

}
