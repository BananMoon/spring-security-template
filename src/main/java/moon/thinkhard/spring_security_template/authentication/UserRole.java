package moon.thinkhard.spring_security_template.authentication;

public enum UserRole {
    MEMBER_SUPER("ROLE_MEMBER_SUPER"),
    MEMBER_GENERAL("ROLE_MEMBER_GENERAL"),
    NON_MEMBER("ROLE_NON_MEMBER")
    ;

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
