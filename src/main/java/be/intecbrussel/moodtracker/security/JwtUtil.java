package be.intecbrussel.moodtracker.security;

import be.intecbrussel.moodtracker.models.Client;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final String SECRET_KEY = Encoders.BASE64.encode(KEY.getEncoded());
    private final long ACCESS_TOKEN_VALIDITY = TimeUnit.HOURS.toMillis(1);
    private final JwtParser jwtParser;
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .build();
    }

    public String createAccessToken(Client client) {
        Claims claims = Jwts.claims().setSubject(client.getEmail());

        claims.put("roles", client.getRole().stream().map(Enum::name).collect(Collectors.toList()));

        Date tokenValidation= new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidation)
                .signWith(KEY, SignatureAlgorithm.HS512)
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

