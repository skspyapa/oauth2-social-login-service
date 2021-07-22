package lk.sky360solutions.authentication.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lk.sky360solutions.authentication.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenServiceImpl implements TokenService {

  @Value("${jwt.secret}")
  private String secret;

  @Override
  public String getUsernameByToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public Map<String, Object> getAllClaimsByToken(String token) {
    Claims claims = extractAllClaims(token);
    Map<String, Object> claimMap = new HashMap<>();
    claims.keySet().forEach(key -> {
      claimMap.put(key, claims.get(key));
    });
    return claimMap;
  }

  @Override
  public String crate(String username, Map<String, Object> customParams) {
    return createToken(customParams, username);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  @Override
  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60))) // expire after one minute
      .signWith(SignatureAlgorithm.HS256, secret).compact();
  }

}
