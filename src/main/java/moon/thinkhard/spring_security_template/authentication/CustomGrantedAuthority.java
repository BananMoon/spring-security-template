package moon.thinkhard.spring_security_template.authentication;

import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {
    private final UserRole role;

    public CustomGrantedAuthority(UserRole role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
