package com.example.vacancyapp.jwt;

import com.example.vacancyapp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt-secret-key}")
    private String secretKey;

    @Value("${jwt-exp-time}")
    private long expTime;

    public String getToken(User user){
        String token= Jwts.builder()
                .setSubject(user.getMail())
                .setIssuer("token")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(expTime)))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        return token;
    }

    public String getMailFromToken(String token){
        Claims claims=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String mail=claims.getSubject();
        return mail;
    }
}
