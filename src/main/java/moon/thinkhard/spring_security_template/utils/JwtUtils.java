package moon.thinkhard.spring_security_template.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import moon.thinkhard.spring_security_template.authentication.CustomAuthenticationDetails;
import moon.thinkhard.spring_security_template.authentication.config.JwtProperties;
import moon.thinkhard.spring_security_template.domain.account.AccountType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {
    private static final Long EXPIRED_IN_MILLI = 3_600_000L;
    private static final String CLAIMS_USERNAME_KEY = "username";
    private static final String CLAIMS_ACCOUNT_TYPE_KEY = "accountType";
    private final Key signingKey;
    private final Log logger;

    public JwtUtils(JwtProperties jwtProperties) {
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
        this.logger = LogFactory.getLog(getClass());
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
//        return getJwtClaims(token).get(CLAIMS_ACCOUNT_TYPE_KEY, AccountType.class);
        // TODO 임시 조치.
        return AccountType.MEMBER;
    }

    private Claims getJwtClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Optional<String> resolveJwt(HttpServletRequest request) {
        String valueFromAuthHeader = request.getHeader("Authorization");
        if (StringUtils.isBlank(valueFromAuthHeader)) {
            return Optional.empty();
        }
        return Optional.of(valueFromAuthHeader.substring(7));
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            logger.error("Jwt is Expired!!");
            return false;
        } catch (JwtException e) {
            logger.error("Jwt exception is thrown !!");
        }
        return true;
    }
}
