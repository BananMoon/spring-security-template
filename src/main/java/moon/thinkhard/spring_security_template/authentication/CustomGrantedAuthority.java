package moon.thinkhard.spring_security_template.authentication;

import moon.thinkhard.spring_security_template.domain.account.AccountType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public class CustomGrantedAuthority implements GrantedAuthority {
    private static final String ROLE_PREFIX ="ROLE_";

    private final String auth;

    protected CustomGrantedAuthority(String auth) {
        this.auth = auth;
    }

    public static CustomGrantedAuthority from(AccountType auth) {
        return new CustomGrantedAuthority(ROLE_PREFIX + auth.name());
    }

    @Override
    public String getAuthority() {
        return auth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomGrantedAuthority that = (CustomGrantedAuthority) o;
        return Objects.equals(auth, that.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auth);
    }
}
