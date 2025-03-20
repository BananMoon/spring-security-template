package moon.thinkhard.spring_security_template.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import moon.thinkhard.spring_security_template.authentication.CustomAuthenticationDetails;
import moon.thinkhard.spring_security_template.authentication.config.JwtProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {
    @Mock
    private JwtProperties jwtProperties;

    @DisplayName("필요한 값을 JWT 토큰의 Claims에 세팅한다.")
    @Test
    void createJwt() {
        String secretKey = "springsecuritytemplatejwtsecretkey";
        given(jwtProperties.getSecretKey()).willReturn(secretKey);
        JwtUtils jwtUtils = new JwtUtils(jwtProperties);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("accountType", "MEMBER");

        String actual = jwtUtils.createJwt("userA", new CustomAuthenticationDetails(request));

        assertThat(Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseClaimsJws(actual).getBody())
                .containsEntry("username", "userA")
                .containsEntry("accountType", "MEMBER");
    }
}
