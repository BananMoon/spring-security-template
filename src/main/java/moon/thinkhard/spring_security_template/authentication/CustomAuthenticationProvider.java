package moon.thinkhard.spring_security_template.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * CustomAuthenticationToken 객체에 대해 인증 처리하는 클래스.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Log logger;
    private final UserDetailsService userDetailsService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        this.logger = LogFactory.getLog(getClass());
        this.userDetailsService = userDetailsService;
    }

    /**
     * 인증을 수행한다.
     * @param authentication 인증 전 Authentication 객체
     * @return credentials를 포함하는 완전히 인증된 객체.
     * 만약 지원할 수 없는 Authentication이라면 null을 반환할 수 있다.
     * 이런 경우, 다음 AuthenticationProvider 가 주어진 인증 객체를 지원할 수 있는지 시도한다.
     * @throws AuthenticationException 인증에 실패할 경우 발생한다.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("Invoking CustomAuthenticationProvider.authenticate(Authentication)");
        if (!supports(authentication.getClass())) {
            return null;
        }
        // authentication isInstanceOf check
/*
        Assert.isInstanceOf(CustomAuthenticationToken.class, authentication,
                () -> this.messages.getMessage("CustomAuthenticationProvider.onlySupports",
                        "Only CustomAuthenticationToken is supported"));
*/

        String username = determineUsername(authentication);
        UserDetails user = retrieveUser(username);
        return createSuccessAuthentication(username, authentication, user);
    }

    /**
     * 인증에 성공하는 경우 필요한 사용자 정보를 담을 수 있는 Custom Authentication 객체를 생성하여 반환한다.
     * @param principal 사용자 관련 정보 (id/username, 사용자 이름 ..)
     * @param authentication 인증 전 Authentication 객체
     * @param user 사용자 정보를 담는 UserDetails 객체
     * @return 인증에 성공한 Authentication 객체
     */
    private Authentication createSuccessAuthentication(String principal, Authentication authentication, UserDetails user) {
        // TODO password encoding 여부 결정 필요.
        // this.passwordEncoder.encode(authentication.getCredentials())
        logger.debug("Authenticated User");
        return CustomAuthenticationToken.authenticated(principal, authentication.getCredentials(), user.getAuthorities());
    }

    private String determineUsername(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    }

    /**
     * UserDetailsService 를 통해 UserDetails 객체를 조회해서 반환한다.
     * @param username 사용자 username
     * @return username에 해당하는 UserDetails
     */
    private UserDetails retrieveUser(String username) {
        try {
            UserDetails loadedUser = this.userDetailsService.loadUserByUsername(username);

            if (loadedUser == null) {
                throw new UsernameNotFoundException(String.format("UserDetailsService returned null. (username: '%s')", username));
            }
            return loadedUser;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    /**
     * CustomAuthenticationProvider가 지원하는 Authentication 객체인지 판단한다.
     * @param authentication Authentication 객체 구현체
     * @return 인증 처리를 지원하는 Authentication 객체라면 True
     */
    @Override
    public boolean supports(Class<?> authentication) {
        logger.info("Invoking CustomAuthenticationProvider.supports(Class<?>)");

        return (CustomAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
