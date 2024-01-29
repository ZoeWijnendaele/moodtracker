package be.intecbrussel.moodtracker.security;

import be.intecbrussel.moodtracker.models.Client;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey";
    private final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000;
    private final JwtParser jwtParser;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))).build();
    }

    public String createAccessToken(Client client) {
        Claims claims = Jwts.claims().setSubject(client.getEmail());

        claims.put("role", client.getRole().toString());

        Date tokenCreationDate = new Date();
        Date tokenExpirationDate = new Date(tokenCreationDate.getTime() +
                TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_VALIDITY));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenExpirationDate)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.ES256)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest request) {

        try {
            String token = resolveToken(request);

            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;

        } catch (ExpiredJwtException expiredJwtException) {
            request.setAttribute("expired", expiredJwtException.getMessage());
            throw expiredJwtException;
        } catch (Exception exception) {
            request.setAttribute("invalid", exception.getMessage());
            throw exception;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);

        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {

        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception exception) {
            throw exception;
        }
    }

}

