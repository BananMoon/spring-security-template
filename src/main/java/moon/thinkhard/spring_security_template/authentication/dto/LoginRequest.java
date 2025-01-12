package moon.thinkhard.spring_security_template.authentication.dto;


public class LoginRequest {
    private String id;
    private String password;

    protected LoginRequest() {
    }

    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
