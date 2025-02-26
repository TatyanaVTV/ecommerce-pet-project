package ru.petproject.ecommerce.order_service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public static final String SECRET_KEY = "wbltrfpjohgresvkfpucnfvzvenpxyatqgrmgughzga";

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}