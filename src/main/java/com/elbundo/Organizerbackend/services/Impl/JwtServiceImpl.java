package com.elbundo.Organizerbackend.services.Impl;

import com.elbundo.Organizerbackend.dto.TokenPair;
import com.elbundo.Organizerbackend.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    public String extractLogin(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("login", String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractLogin(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(User user, long lifetime) {
        Map<String, Object> map = new HashMap<>();
        map.put("login", user.getUsername());
        map.put("name", user.getName());
        map.put("role", user.getRole());
        return generateToken(map, user, lifetime);
    }

    public String generateAccessToken(User user) {
        return generateToken(user, 1000 * 60 * 30);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, 1000 * 60 * 60 * 30);
    }

    public TokenPair generateTokens(User user) {
        return new TokenPair(generateAccessToken(user), generateRefreshToken(user));
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User user,
            long lifetime
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
