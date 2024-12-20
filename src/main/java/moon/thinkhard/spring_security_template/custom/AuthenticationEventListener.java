package moon.thinkhard.spring_security_template.custom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {
    private final Log logger;

    public AuthenticationEventListener() {
        this.logger = LogFactory.getLog(getClass());
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        this.logger.info("Invoking AuthenticationEventListener.onSuccess(success)");
        logAuthentication(success.getAuthentication());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        this.logger.info("Invoking AuthenticationEventListener.onFailure(failures)");
        logAuthentication(failures.getAuthentication());
    }

    private void logAuthentication(Authentication authentication) {
        this.logger.info(String.format("===> isAuthenticated : %s", authentication.isAuthenticated()));
        this.logger.info(String.format("===> authentication : %s", authentication.getCredentials()));
        this.logger.info(String.format("===> authorities : %s", authentication.getAuthorities()));
        this.logger.info(String.format("===> principal : %s", authentication.getPrincipal()));
    }

}
