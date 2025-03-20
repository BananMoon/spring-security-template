package moon.thinkhard.spring_security_template.authentication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtProperties {
    @Value("${jwt.secret-key}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

}
