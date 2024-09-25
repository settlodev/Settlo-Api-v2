package co.tz.settlo.api;

import co.tz.settlo.api.util.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtHelper {
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final int ACCESS_TOKEN_EXPIRATION_HOURS = 1;
    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    private static JwtHelper instance;

    private JwtHelper() {}

    public static JwtHelper getInstance() {
        if (instance == null) {
            instance = new JwtHelper();
        }
        return instance;
    }

    public String generateAccessToken(String email) {
        return generateToken(email, ACCESS_TOKEN_EXPIRATION_HOURS, ChronoUnit.HOURS);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS);
    }

    private String generateToken(String email, int amount, ChronoUnit unit) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(amount, unit)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Claims getTokenBody(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }

    public String getTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    public static long getRefreshTokenValidity() {
        return ChronoUnit.DAYS.getDuration().toMillis() * REFRESH_TOKEN_EXPIRATION_DAYS;
    }
}