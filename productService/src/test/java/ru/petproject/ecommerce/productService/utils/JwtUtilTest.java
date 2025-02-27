package ru.petproject.ecommerce.productService.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secretKey = "your-secret-key"; // Замените на ваш секретный ключ

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", secretKey); // Устанавливаем секретный ключ через рефлексию
    }

    @Test
    void extractAllClaims_ValidToken() {
        String token = generateToken("user1");

        Claims claims = jwtUtil.extractAllClaims(token);
        assertNotNull(claims);
        assertEquals("user1", claims.getSubject());
    }

    @Test
    void extractUserId_ValidToken() {
        String token = generateToken("user1");

        String userId = jwtUtil.extractUserId(token);
        assertEquals("user1", userId);
    }

    @Test
    void isTokenExpired_ValidToken() {
        String token = generateToken("user1");

        Claims claims = jwtUtil.extractAllClaims(token);
        assertFalse(jwtUtil.isTokenExpired(claims));
    }

//    @Test
//    void isTokenExpired_ExpiredToken() {
//        String expiredToken = generateExpiredToken("user1");
//
//        try {
//            Claims claims = jwtUtil.extractAllClaims(expiredToken);
//            System.out.println("Token claims: " + claims);
//            assertTrue(jwtUtil.isTokenExpired(claims));
//        } catch (Exception e) {
//            System.out.println("Error during token validation: " + e.getMessage());
//            fail("Exception should not be thrown during token validation");
//        }
//    }

    @Test
    void isTokenValid_ValidToken() {
        String token = generateToken("user1");

        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void isTokenValid_InvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtUtil.isTokenValid(invalidToken));
    }

    private String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 часов
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String generateExpiredToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 10)) // 10 часов назад
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 час назад
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}