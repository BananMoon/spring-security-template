package moon.thinkhard.spring_security_template.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moon.thinkhard.spring_security_template.authentication.config.JwtProperties;
import moon.thinkhard.spring_security_template.handler.CustomAuthenticationFailureHandler;
import moon.thinkhard.spring_security_template.handler.CustomAuthenticationSuccessHandler;
import moon.thinkhard.spring_security_template.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
    private static final String SPRING_SECURITY_FORM_ID_KEY = "id";
    private static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(new JwtUtils(jwtProperties)));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String id = this.obtainId(request);
        String password = this.obtainPassword(request);
        CustomAuthenticationDetails details = this.obtainDetails(request);

        CustomAuthentication authRequest = CustomAuthentication.unauthenticated(
                id != null ? id.trim() : "",
                password != null ? password : "",
                details);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainId(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_ID_KEY);
    }
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
    }
    protected CustomAuthenticationDetails obtainDetails(HttpServletRequest request) {
        return new CustomAuthenticationDetails(request);
    }

}
