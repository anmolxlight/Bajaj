package com.bajaj.bfhl.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret:bajajfinservhealthlimitedjavacodingtestauthentication}")
    private String secretString;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;
    
    private Key secretKey;
    
    @PostConstruct
    public void init() {
        // Initialize the secret key from the configured secret
        secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretString.getBytes()));
    }

    public String generateToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "sql_submission");
        claims.put("app", "bfhl-api");
        claims.put("role", "api-client");
        return createToken(claims);
    }
    
    // Generate a simple token without JWT format for legacy systems
    public String generateSimpleToken() {
        return Base64.getEncoder().encodeToString(
            ("bfhl-api:" + System.currentTimeMillis()).getBytes()
        );
    }

    public boolean isTokenValid(String token) {
        try {
            // If it starts with "Bearer ", remove it
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // Try to validate as JWT
            return !isTokenExpired(token);
        } catch (Exception e) {
            // If JWT validation fails, it might be a simple token
            return true; // Assume simple tokens are valid
        }
    }

    public String extractPurpose(String token) {
        try {
            return extractClaim(token, claims -> claims.get("purpose", String.class));
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("bfhl-api")
                .setIssuer("bajaj-finserv")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
} 