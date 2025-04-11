package moon.thinkhard.spring_security_template.authentication.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 인증이 완료되었으나, 요청한 리소스에 접근할 권한이 없을 때 AccessDeniedException이 발생하며, 이 핸들러가 이를 처리합니다.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final Log logger;

    public CustomAccessDeniedHandler() {
        this.logger = LogFactory.getLog(getClass());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        logger.info("Invoking CustomAccessDeniedHandler.handle(request, response, accessDeniedException)");

        setResponse(response);
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.sendRedirect("/loginPage.html");
    }
}
