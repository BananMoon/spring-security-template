package moon.thinkhard.spring_security_template.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * CustomAuthenticationToken 객체에 대해 인증 처리하는 클래스.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Log logger;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsChecker userDetailsChecker;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.logger = LogFactory.getLog(getClass());
        this.userDetailsService = userDetailsService;
        this.userDetailsChecker = new CustomUserDetailsChecker();
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
        return internalAuthenticate((CustomAuthentication) authentication);
    }

    private CustomAuthentication internalAuthenticate(CustomAuthentication authentication) throws AuthenticationException {
        logger.info("Invoking CustomAuthenticationProvider.authenticate(Authentication)");
        if (!supports(authentication.getClass())) {
            return null;
        }

        String username = determineUsername(authentication);
        CustomAuthenticationDetails details = determineDetails(authentication);

        CustomUserDetails user = retrieveUser(username);
        this.userDetailsChecker.check(user);
        additionalAuthenticationChecks(user, authentication);

        return createSuccessAuthentication(username, details, user);
    }

    /**
     * 패스워드 검증한다.
     * @param user DB에서 조회한 사용자
     * @param authentication 요청자가 입력한 인증 정보
     * @throws BadCredentialsException password가 null이거나 입력받은 password가 일치하지 않는 경우 발생한다.
     */
    private void additionalAuthenticationChecks(UserDetails user, Authentication authentication) {

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("Failed to authenticate since no credentials provided");
        }
        String presentedPassword = authentication.getCredentials().toString();

        if (!this.passwordEncoder.matches(presentedPassword, user.getPassword())) {
            throw new BadCredentialsException("Failed to authenticate since password does not match stored value");
        }
    }

    /**
     * 인증에 성공하는 경우 필요한 사용자 정보를 담을 수 있는 Custom Authentication 객체를 생성하여 반환한다.
     * @param principal 사용자 관련 정보 (id/username, 사용자 이름 ..)
     * @param user 사용자 정보를 담는 UserDetails 객체
     * @return 인증에 성공한 Authentication 객체
     */
    private CustomAuthentication createSuccessAuthentication(String principal, CustomAuthenticationDetails details, CustomUserDetails user) {
        logger.debug("Authenticated User");

        return CustomAuthentication.authenticated(principal, details, user.getAuthorities());
    }

    private String determineUsername(CustomAuthentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    }

    private CustomAuthenticationDetails determineDetails(CustomAuthentication authentication) {
        return (CustomAuthenticationDetails) authentication.getDetails();
    }

    /**
     * UserDetailsService 를 통해 UserDetails 객체를 조회해서 반환한다.
     * @param username 사용자 username
     * @return username에 해당하는 UserDetails
     */
    private CustomUserDetails retrieveUser(String username) {
        try {
            return (CustomUserDetails) this.userDetailsService.loadUserByUsername(username);
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

        return (CustomAuthentication.class.isAssignableFrom(authentication));
    }

    private static class CustomUserDetailsChecker implements UserDetailsChecker {
        private final Log logger;

        private CustomUserDetailsChecker() {
            this.logger = LogFactory.getLog(getClass());
        }

        @Override
        public void check(UserDetails user) {
            this.internalCheck((CustomUserDetails) user);
        }

        private void internalCheck(CustomUserDetails user) {
            if (!user.isAccountNonLocked()) {
                this.logger.debug("Failed to authenticate since user account is locked");
                throw new LockedException("[AbstractUserDetailsAuthenticationProvider.locked User account is locked");
            }
            if (!user.isEnabled()) {
                this.logger.debug("Failed to authenticate since user account is disabled");
                throw new DisabledException("[AbstractUserDetailsAuthenticationProvider.disabled] User is disabled");
            }
            if (!user.isAccountNonExpired()) {
                this.logger.debug("Failed to authenticate since user account has expired");
                throw new AccountExpiredException("[AbstractUserDetailsAuthenticationProvider.expired] User account has expired");
            }
        }

    }
}
