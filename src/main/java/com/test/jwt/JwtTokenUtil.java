package com.test.jwt;

import com.test.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {


    private static final long EXPIRE_DURATION = 24 * 60 * 60*1000; //24 hours

    @Value("${app.jwt.secret}") //instructing spring to inject value of access token to this field.
    private String secretKey;

    public String generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(user.getId()+" , "+user.getEmail())
                .setIssuer("ManikSetia")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
