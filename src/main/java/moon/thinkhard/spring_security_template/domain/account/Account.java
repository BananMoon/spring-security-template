package moon.thinkhard.spring_security_template.domain.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    // TODO 저장 형태.
    private String password;
    private boolean locked;
    private char useYn;

    public Account(String email, String password, boolean locked, char useYn) {
        this.email = email;
        this.password = password;
        this.locked = locked;
        this.useYn = useYn;
    }

    protected Account() {
    }

    public boolean isValid() {
        return !this.locked && this.useYn == 'Y';
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isEnabled() {
        return this.useYn == 'Y';
    }
}
