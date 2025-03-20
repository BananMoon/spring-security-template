package moon.thinkhard.spring_security_template.domain.account;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private boolean locked;
    private char useYn;

    @Enumerated(EnumType.STRING)
    private AccountType auth;

    public Account(String email, String password, boolean locked, char useYn, AccountType auth) {
        this.email = email;
        this.password = password;
        this.locked = locked;
        this.useYn = useYn;
        this.auth = auth;
    }

    protected Account() {
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isEnabled() {
        return this.useYn == 'Y';
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
    public AccountType getAuth() {
        return auth;
    }
}
