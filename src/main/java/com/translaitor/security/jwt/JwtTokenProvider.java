package com.translaitor.security.jwt;

import com.translaitor.model.User;
import com.translaitor.model.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Log
@Component
public class JwtTokenProvider {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT ";

    @Value("{jwt.secret}")
    private String jwtSecret;

    @Value("{jwt.token-expiration}")
    private int jwtTokenDurationTokenInSeconds;

    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Date tokenExpirationDate = new Date(System.currentTimeMillis() + (jwtTokenDurationTokenInSeconds * 1000));

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(tokenExpirationDate)
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles().stream()
                        .map(UserRole::name)
                        .collect(Collectors.joining(", "))
                )
                .compact();
    }

    public Long getUserIdFromJWT(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {

        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.info("Token signing error: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.info("Malformed token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.info("Token has expired: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.info("Token not supported: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims empty");
        }

        return false;
    }
}
