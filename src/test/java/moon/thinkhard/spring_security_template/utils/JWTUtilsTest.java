package moon.thinkhard.spring_security_template.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import moon.thinkhard.spring_security_template.domain.account.AccountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class JWTUtilsTest {

    @DisplayName("필요한 값을 JWT 토큰의 Claims에 세팅한다.")
    @Test
    void createJwt() {
        JWTUtils jwtUtils = new JWTUtils("springsecuritytemplatejwtsecretkey");
        SecretKey secretKey = Keys.hmacShaKeyFor("springsecuritytemplatejwtsecretkey".getBytes());

        String result = jwtUtils.createJwt("userA", List.of(AccountType.MEMBER), 3_600_000L);

        assertThat(Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(result).getBody())
                .containsAnyOf(entry("roles", List.of("MEMBER")), entry("username", "userA"));
    }
}
