package moon.thinkhard.spring_security_template.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Log logger;

    public CustomAuthenticationEntryPoint() {
        this.logger = LogFactory.getLog(getClass());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.info("Invoking CustomAuthenticationEntryPoint.commence(request, response, authenticationException)");

        setResponse(response);
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/plain;charset=UTF-8");
        response.getWriter().write("🤖 Authorization is failed. Go to Login Page.");
        response.sendRedirect("/loginPage.html");
    }
}
