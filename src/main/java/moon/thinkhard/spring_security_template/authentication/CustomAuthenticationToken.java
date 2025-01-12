package moon.thinkhard.spring_security_template.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class CustomAuthenticationToken implements Authentication {
    private final Object principal;
    private final Object credentials;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomAuthenticationToken(Object principal, Object credentials) {
        this(principal, credentials, AuthorityUtils.NO_AUTHORITIES);
    }

    private CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
    }

    public static CustomAuthenticationToken authenticated(Object principal, Object credentials,
                                                          Collection<? extends GrantedAuthority> authorities) {
        return new CustomAuthenticationToken(principal, credentials, authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
