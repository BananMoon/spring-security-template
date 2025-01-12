package moon.thinkhard.spring_security_template.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 요청한 리소스에 접근할 권한이 없을 때 AccessDeniedException이 발생하며, 이 핸들러가 이를 처리합니다.
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
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/plain;charset=UTF-8");
        response.getWriter().write("🤖 Your access is denied. Please log in again.");
        response.sendRedirect("/loginPage.html");
    }
}
