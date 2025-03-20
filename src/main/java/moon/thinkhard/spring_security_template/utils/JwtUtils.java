package moon.thinkhard.spring_security_template.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import moon.thinkhard.spring_security_template.authentication.CustomAuthenticationDetails;
import moon.thinkhard.spring_security_template.authentication.config.JwtProperties;
import moon.thinkhard.spring_security_template.domain.account.AccountType;

import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private static final Long EXPIRED_IN_MILLI = 3_600_000L;
    private static final String CLAIMS_USERNAME_KEY = "username";
    private static final String CLAIMS_ACCOUNT_TYPE_KEY = "accountType";
    private final Key signingKey;


    public JwtUtils(JwtProperties jwtProperties) {
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String createJwt(Object principal, CustomAuthenticationDetails details) {
        return Jwts.builder()
                .setClaims(userToClaims(principal, details))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRED_IN_MILLI))
                .signWith(signingKey)
                .compact();
    }

    public Claims userToClaims(Object principal, CustomAuthenticationDetails details) {
        String username = (String) principal;

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(CLAIMS_USERNAME_KEY, username);
        claims.put(CLAIMS_ACCOUNT_TYPE_KEY, details.getAccountType());
        return claims;
    }

    public String getUsername(String token) {
        return getJwtClaims(token).get(CLAIMS_USERNAME_KEY, String.class);
    }

    public AccountType getAccountType(String token) {
        return getJwtClaims(token).get(CLAIMS_ACCOUNT_TYPE_KEY, AccountType.class);
    }

    private Claims getJwtClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build()
                .parseClaimsJws(token)
                .getBody();
    }
}
