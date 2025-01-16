package moon.thinkhard.spring_security_template.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * 사용자를 특정하는 데이터를 로드하는 주요 인터페이스로, CustomAuthenticationProvider에서 사용하는 전략이다.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * username에 기반하여 사용자를 반환한다.
     * @param username 데이터가 필요한 사용자를 식별하는 username
     * @return 완전히 채워진 user 데이터 반환 (null이 나오지 않음.)
     * @throws UsernameNotFoundException 사용자가 존재하지 않으면 발생한다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 권한, 잠금 여부, 활성화 여부 조회를 포함한 사용자 정보 조회 SQL
        //  Test Data
        return new CustomUserDetails(username, "password", new HashSet<>(), true, true);
    }
}
