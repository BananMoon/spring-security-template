package moon.thinkhard.spring_security_template.authentication.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * 인증에 시도했는데, 실패했을 때 에러를 핸들링한다.
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final Log logger;

    public CustomAuthenticationFailureHandler() {
        this.logger = LogFactory.getLog(getClass());
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        logger.info("Invoking CustomAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception)");

        setResponse(response);
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write("🤖 Login try has Failed. Please check your input.");
    }
}
