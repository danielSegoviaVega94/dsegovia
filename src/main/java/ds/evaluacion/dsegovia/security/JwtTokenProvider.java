package ds.evaluacion.dsegovia.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    private SecretKey obtenerClaveSecreta() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generarToken(String correo) {
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(correo)
                .issuedAt(ahora)
                .expiration(fechaExpiracion)
                .signWith(obtenerClaveSecreta(), Jwts.SIG.HS512)
                .compact();
    }

    public String obtenerCorreoDelToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(obtenerClaveSecreta())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(obtenerClaveSecreta())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}