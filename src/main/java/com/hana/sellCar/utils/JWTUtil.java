package com.hana.sellCar.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {
    //this is used to extract token , user details ,
    //decode the token etc


    private long ACCESS_TOKEN_EXPIRATION=900000;

    private long REFRESH_TOKEN_EXPIRATION=604800000 ;

    private SecretKey getSigningKey(){
        String SECRET="a4cb27c704b10c930578922eb9d984f1e99be823e31a5bee54ce903db21174613a8afe355721b222360edaa3a472ff699a84aa16754c9f7f35f99da10e0d86a1";
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //specific claims
    private <T> T extractClaims(String token, Function<Claims,T> claimsResolvers){
        final Claims claims=extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }

    public Date extractExpDate(String token){
        return extractClaims(token,Claims::getExpiration);
    }


    private boolean isTokenExpired(String token){
        return extractExpDate(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=extractUsername(token);
        return ((username.equals(userDetails.getUsername()))&&(!isTokenExpired(token)));
    }


    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationTime) {
        return Jwts.builder()
                .claims(extraClaims)
                .setSubject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .toList());
        return generateToken(claims, userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, REFRESH_TOKEN_EXPIRATION);
    }




}
