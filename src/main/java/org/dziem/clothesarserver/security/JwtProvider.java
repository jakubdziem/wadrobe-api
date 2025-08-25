package org.dziem.clothesarserver.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private final String jwtSecret = "mySecretKeymySecretKeymySecretKeymySecretKeymySecretKeymySecretKeymySecretKeymySecretKey";
    private final StringRedisTemplate redisTemplate;

    public JwtProvider(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 10)) // ~10 years
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        String userId = getUserIdFromToken(token);
        Set<String> tokens = redisTemplate.opsForSet().members("userTokens:" + userId);
        return tokens != null && tokens.contains(token);
    }
}
