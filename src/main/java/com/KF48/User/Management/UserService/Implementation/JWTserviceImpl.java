package com.KF48.User.Management.UserService.Implementation;

import com.KF48.User.Management.UserService.JWService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
@Service
public class JWTserviceImpl implements JWService {

    // Expiration Times (can be moved to a config class)
    private static final long JWT_EXPIRATION_TIME = 1000 * 60 * 24; // 24 minutes
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days

    // Generate an access token (JWT)
    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME)) // 24 minutes
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Helper method to extract a specific claim from the token
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract the username (subject) from the token
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())  // ✅ fixed typo
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e); // Improved error handling
        }
    }

    // Get the signing key for HMAC signature algorithm
    private Key getSigningKey() {
        // Consider loading this from environment variables in production
        byte[] keyBytes = Decoders.BASE64.decode("v4Vm4LmH5oThbJ+AtHl6UC0WxuGUe5S0YJKf2HzP85A=");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Method to check if the token is valid
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        // Token is valid if the username matches and the token is not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        // Returns true if the token is expired
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Generate a refresh token
    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME)) // 7 days
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
