package com.immanuel.app.web;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secretKey = "C30B75E8F5DED9A8F7E4EB4E6C8D11BAA991DB0F55ADCD835F73C7EF02AAEF19B96C109568E65A09566218C4A214ED7042321FFD98C9CAC3DA470B34B9F557A4";
    private final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(decodedKey())
                .compact();
    }

    private SecretKey decodedKey() {
        byte[] dKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(dKey);
    }

    public String extractUsername(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        return claims.getSubject();
    }

    private Claims getClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(decodedKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean isValid(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        return claims.getExpiration().after(claims.getIssuedAt());
    }
}
