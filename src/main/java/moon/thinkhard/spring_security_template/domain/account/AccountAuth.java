package moon.thinkhard.spring_security_template.domain.account;

import moon.thinkhard.spring_security_template.authentication.CustomGrantedAuthority;

import java.util.Set;

public enum AccountAuth {
    GENERAL,
    SUPER;


    public Set<CustomGrantedAuthority> toGrantedAuthorities() {
        return Set.of(CustomGrantedAuthority.from(this));
    }
}
