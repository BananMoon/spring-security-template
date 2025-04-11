package moon.thinkhard.spring_security_template.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 인증되지 않은 사용자가 보호되는 리소스에 접근하여 인증에 실패했을 때 에러를 핸들링한다.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Log logger;

    public CustomAuthenticationEntryPoint() {
        this.logger = LogFactory.getLog(getClass());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.info("Invoking CustomAuthenticationEntryPoint.commence(request, response, authenticationException)");
        logger.info("🤖Non authenticated user can't access the protected resources.");

        setResponse(response);
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect("/loginPage.html");
    }
}
