package moon.thinkhard.spring_security_template.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moon.thinkhard.spring_security_template.authentication.CustomAuthenticationDetails;
import moon.thinkhard.spring_security_template.utils.JwtUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String TOKEN_RESPONSE_KEY = "token";

    private final Log logger;
    private final JwtUtils jwtUtils;

    public CustomAuthenticationSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        this.logger = LogFactory.getLog(getClass());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        logger.info("Invoking CustomAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication)");

        setResponseWithJwt(response, jwtUtils.createJwt(authentication.getPrincipal(), (CustomAuthenticationDetails) authentication.getDetails()));
    }

    private void setResponseWithJwt(HttpServletResponse response, String jwtToken) throws IOException {
        logger.info(String.format("🤖👏 You have now successfully logged in. (token : %s)", jwtToken));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put(TOKEN_RESPONSE_KEY, jwtToken);
        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }
}
