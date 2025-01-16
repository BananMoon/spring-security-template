package moon.thinkhard.spring_security_template.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moon.thinkhard.spring_security_template.authentication.dto.LoginRequest;
import moon.thinkhard.spring_security_template.handler.CustomAuthenticationFailureHandler;
import moon.thinkhard.spring_security_template.handler.CustomAuthenticationSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.objectMapper = new ObjectMapper();
        setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        LoginRequest requestBody = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        Authentication authRequest = new CustomAuthenticationToken(requestBody.getId(), requestBody.getPassword());
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
