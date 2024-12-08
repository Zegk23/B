package com.massmotosperu.backend.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generarToken(Map<String, Object> userClaims) {
        return Jwts.builder()
                .setClaims(userClaims) // Agregar los datos del usuario como claims
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día de duración
                .signWith(key)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
