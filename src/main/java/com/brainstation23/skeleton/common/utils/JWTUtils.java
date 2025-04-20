package com.brainstation23.skeleton.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JWTUtils {

    public static String generateToken(String userName, String expiryTime, String jwtSecretKey) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, expiryTime, jwtSecretKey);
    }

    public static String generateToken(Map<String, Object> claims,
                                       String userName,
                                       String expiryTime,
                                       String jwtSecretKey) {
        return createToken(claims, userName, expiryTime, jwtSecretKey);
    }

    public static String extractUserName(String token, String jwtSecretKey) {
        return extractClaim(token, jwtSecretKey, Claims::getSubject);
    }

    public static <T> T extractClaimByKey(String token, String jwtSecretKey, String key, Class<T> classType) {
        return extractClaim(token, jwtSecretKey, claims -> claims.get(key, classType));
    }

    public static Date extractExpiration(String token, String jwtSecretKey) {
        return extractClaim(token, jwtSecretKey, Claims::getExpiration);
    }

    public static String trimToken(String barerToken, String jwtTokenPrefix) {
        String tokenPrefix = String.format("%s ", jwtTokenPrefix);
        return StringUtils.replace(barerToken, tokenPrefix, "");
    }

    public static boolean validateToken(String token,
                                        String userName,
                                        String jwtSecretKey
    ) {
        return (
                StringUtils.equals(extractUserName(token, jwtSecretKey), userName)
                        && !isTokenExpired(token, jwtSecretKey)
        );
    }

    private static String createToken(Map<String, Object> claims,
                                      String subject,
                                      String expiryTime,
                                      String jwtSecretKey) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getExpiryMilli(expiryTime)))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    private static int getExpiryMilli(String expiryTime) {
        int expMinutes = Integer.parseInt(expiryTime);
        return DateTimeUtils.convertToMilli(expMinutes, Calendar.MINUTE);
    }

    private static <T> T extractClaim(String token,
                                      String jwtSecretKey,
                                      Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, jwtSecretKey);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token, String jwtSecretKey) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenExpired(String token, String jwtSecretKey) {
        return extractExpiration(token, jwtSecretKey).before(new Date());
    }

}
