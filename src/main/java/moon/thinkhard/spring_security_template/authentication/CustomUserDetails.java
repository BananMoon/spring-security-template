package moon.thinkhard.spring_security_template.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Set;

public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;

    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonLocked;
    private final boolean enabled;


    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             boolean accountNonLocked, boolean enabled) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");

        this.username = username;
        this.password = password;
        this.authorities = Set.copyOf(authorities);
        this.accountNonLocked = accountNonLocked;
        this.enabled = enabled;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled();
    }
}
