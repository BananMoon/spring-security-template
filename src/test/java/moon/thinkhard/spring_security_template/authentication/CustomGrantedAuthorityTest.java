package moon.thinkhard.spring_security_template.authentication;

import moon.thinkhard.spring_security_template.domain.account.AccountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class CustomGrantedAuthorityTest {

    @ParameterizedTest
    @EnumSource(AccountType.class)
    @DisplayName("AccountType으로 Security의 권한 문자열을 반환한다.")
    void from(AccountType given) {
        CustomGrantedAuthority result = CustomGrantedAuthority.from(given);

        assertThat(result).isEqualTo(new CustomGrantedAuthority("ROLE_" + given.name()));
    }
}
