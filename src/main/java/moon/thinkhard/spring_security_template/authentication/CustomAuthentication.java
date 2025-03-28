package moon.thinkhard.spring_security_template.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

/**
 * Spring Security에서 사용자 인증을 표현한다. 사용자의 자격증명을 캡슐화하고 인증상태를 관리하는 것이 목적이다.
 */
public class CustomAuthentication implements Authentication {
    /**
     * 사용자와 관련된 정보로, 일반적으로 인증 전에는 사용자 이름(id/username), 인증 후에는 UserDetails 객체 혹은 사용자 이름이 세팅된다.
     */
    private final Object principal;
    /**
     * 인증을 위해 제출된 자격증명으로, 일반적으로 인증 전에는 password가 세팅된다. 인증 후에는 보안 상 null이 세팅될 수 있다.
     */
    private final Object credentials;

    /**
     * 사용자와 관련된 정보 (사용자 타입)로, 인증 후에도 값이 세팅된다.
     */
    private final CustomAuthenticationDetails details;
    /**
     * 사용자의 권한 정보로, 인증 전에는 빈 Collection, 인증 후에는 사용자의 권한 값이 세팅된다.
     */
    private final Collection<? extends GrantedAuthority> authorities;

    private CustomAuthentication(Object principal, Object credentials, CustomAuthenticationDetails details,
                                 Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.details = details;
        this.authorities = authorities;
    }

    /**
     * 인증 전 객체를 생성한다.
     *
     * @param principal   사용자 관련 정보 (id/username, 사용자 이름 ..)
     * @param credentials 사용자 패스워드 정보
     * @return 인증 시도하는 Authentication 객체
     */
    public static CustomAuthentication unauthenticated(Object principal, Object credentials, CustomAuthenticationDetails details) {
        return new CustomAuthentication(principal, credentials, details, AuthorityUtils.NO_AUTHORITIES);
    }

    /**
     * 인증에 성공한 객체를 생성한다.
     * 패스워드는 세팅하지 않는다.
     *
     * @param principal   사용자 관련 정보 (id/username, 사용자 이름 ..)
     * @param authorities 사용자의 권한 정보
     * @return 인증에 성공한 Authentication 객체
     */
    public static CustomAuthentication authenticated(Object principal, CustomAuthenticationDetails details,
                                                     Collection<? extends GrantedAuthority> authorities) {
        return new CustomAuthentication(principal, null, details, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    /**
     * @return 요청 데이터/정보로부터 알 수 있는 정보 (IP, 요청 Client 정보-RemoteAddr, SessionId 등) 객체 <br>
     * ex) WebAuthenticationDetails
     */
    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    /**
     * 인증 필터를 직접 커스터마이징할 경우, 호출되지 않음.
     *
     * @return 인증에 성공한 객체인지 여부
     */
    @Override
    public boolean isAuthenticated() {
        return false;
    }

    /**
     * 인증 필터를 직접 커스터마이징할 경우, 호출되지 않음.
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return principal.toString();
    }
}
