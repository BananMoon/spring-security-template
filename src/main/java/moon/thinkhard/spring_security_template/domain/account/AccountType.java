package moon.thinkhard.spring_security_template.domain.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import moon.thinkhard.spring_security_template.authentication.CustomGrantedAuthority;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Set;

public enum AccountType {
    NONMEMBER("ACNT001"),
    MEMBER("ACNT002");

    private final String dbCode;

    AccountType(String dbCode) {
        this.dbCode = dbCode;
    }

    public Set<CustomGrantedAuthority> toGrantedAuthorities() {
        return Set.of(CustomGrantedAuthority.from(this));
    }

    @JsonCreator
    public static AccountType from(String value) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.name().equals(value)) {
                return accountType;
            }
        }
        throw new InternalAuthenticationServiceException(value);
    }

}
