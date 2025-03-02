package moon.thinkhard.spring_security_template.authentication;

import jakarta.servlet.http.HttpServletRequest;
import moon.thinkhard.spring_security_template.domain.account.AccountType;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Objects;


public class CustomAuthenticationDetails extends WebAuthenticationDetails {
    private static final String SPRING_SECURITY_FORM_ACCOUNT_TYPE_KEY = "accountType";

    private final AccountType accountType;

    public CustomAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.accountType = AccountType.from(request.getParameter(SPRING_SECURITY_FORM_ACCOUNT_TYPE_KEY));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomAuthenticationDetails that = (CustomAuthenticationDetails) o;
        return accountType == that.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountType);
    }
}
