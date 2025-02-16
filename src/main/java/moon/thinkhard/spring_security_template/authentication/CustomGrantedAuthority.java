package moon.thinkhard.spring_security_template.authentication;

import moon.thinkhard.spring_security_template.domain.account.AccountAuth;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {
    private static final String MEMBER_ROLE_PREFIX ="ROLE_MEMBER_";

    private final String auth;

    private CustomGrantedAuthority(String auth) {
        this.auth = auth;
    }

    public static CustomGrantedAuthority from(AccountAuth auth) {
        return new CustomGrantedAuthority(MEMBER_ROLE_PREFIX + auth.name());
    }

    @Override
    public String getAuthority() {
        return auth;
    }
}
