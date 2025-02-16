package moon.thinkhard.spring_security_template.authentication;

import moon.thinkhard.spring_security_template.domain.account.Account;
import moon.thinkhard.spring_security_template.domain.account.AccountRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 사용자를 특정하는 데이터를 로드하는 주요 인터페이스로, CustomAuthenticationProvider에서 사용하는 전략이다.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final Log logger;
    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.logger = LogFactory.getLog(getClass());
    }

    /**
     * username에 기반하여 사용자를 반환한다.
     * @param username 데이터가 필요한 사용자를 식별하는 username
     * @return 완전히 채워진 user 데이터 반환 (null이 나오지 않음.)
     * @throws UsernameNotFoundException 사용자가 존재하지 않으면 발생한다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails userDetails = this.loadUsersByUsername(username);

        return new CustomUserDetails(username, userDetails.getPassword(), userDetails.getAuthorities(), userDetails.isAccountNonLocked(), userDetails.isEnabled());
    }

    protected CustomUserDetails loadUsersByUsername(String username) {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> {
                    logger.debug("Query returned no results for user '" + username + "'");
                    throw new UsernameNotFoundException(String.format("CutomUserDetailsService.notFound : Username %s not found", username));
                });

        Collection<? extends GrantedAuthority> grantedAuthorities = loadUserAuthorities(account);
        if (grantedAuthorities.isEmpty()) {
            logger.debug(String.format("User '%s' has no authorities and will be treated as 'not found'", username));
            throw new UsernameNotFoundException(String.format("[CutomUserDetailsService.noAuthority] User '%s' has no GrantedAuthority", username));
        }

        return new CustomUserDetails(account.getEmail(), account.getPassword(), grantedAuthorities, account.isLocked(), account.isEnabled());
    }

    private Collection<? extends GrantedAuthority> loadUserAuthorities(Account account) {
        return account.getAuth().toGrantedAuthorities();
    }
}
