package org.propertybiddingsystem.usermanagementsystem.Utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    @Value("${security.jwt.secret-key}")
    private String secretKey ;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;
    private static final long ACCESS_TOKEN_VALIDITY = 5 * 60 * 1000;
    private static  final long REFRESH_TOKEN_VALIDITY= 7 * 24 * 60 * 60 * 1000;
    public  String extractUserName( String token) {
        return extractClaim(token, Claims:: getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final var claims = extractAllClaims( token);
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String generateAccessToken( String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration( new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String   generateRefreshToken( String username ) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))

                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>() , userDetails)
;    }

  public String generateToken( Map<String,Object> extractClaims, UserDetails userDetails) {
        return buildToken( extractClaims, userDetails ,jwtExpiration);
  }


  private String buildToken(Map<String,Object> extractClaims, UserDetails userDetails, long expiration) {
         return Jwts.builder()
                 .setClaims(extractClaims)
                 .setSubject(userDetails.getUsername())
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis() + expiration))
                 .signWith( getSignInKey(), SignatureAlgorithm.HS256)
                 .compact();
  }


  public  boolean isTokenValid( String token , UserDetails userDetails){
        final var username = extractUserName(token);
         return ( username.equals(userDetails.getUsername())) && !isTokenExpired(token) ;
   }

    private boolean isTokenExpired(String token) {
        return  extractExpiration(token).before( new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Key getSignInKey() {
        var keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

}
