package com.effix.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.expiration.token}")
    private long jwtExpirationMs;

    public String generateToken(String subject, String secret) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getSubjectIfValidToken(String token, String secret) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(token).getBody();
            Date now = new Date();
            Date expirationDate = claims.getExpiration();
            if (expirationDate.after(now)) return claims.getSubject();
            else return null;
        } catch (Exception e) {
            return null;
        }
    }
}

