package moon.thinkhard.spring_security_template.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Log logger;

    public CustomAuthenticationProvider() {
        this.logger = LogFactory.getLog(getClass());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("Invoking CustomAuthenticationProvider.authenticate(Authentication)");

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        logger.info("Invoking CustomAuthenticationProvider.supports(Class<?>)");
        logger.info(String.format("authentication class name : %s", authentication.getName()));

        return (CustomAuthenticationToken.class.isAssignableFrom(authentication));
    }

    // retrieveUser() override
    // userDetailsService create(implementation), loadUserByUsername() override
}
