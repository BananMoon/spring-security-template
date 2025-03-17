package moon.thinkhard.spring_security_template.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import moon.thinkhard.spring_security_template.domain.account.AccountType;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.List;

public class JWTUtils {

    private final Key secretKey;

    public JWTUtils(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createJwt(String username, List<AccountType> roles, Long expiredMs) {
        return Jwts.builder()
                .setClaims(userToClaims(username, roles))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Claims userToClaims(String username, List<AccountType> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        claims.put("username", username);
        return claims;
    }

}
