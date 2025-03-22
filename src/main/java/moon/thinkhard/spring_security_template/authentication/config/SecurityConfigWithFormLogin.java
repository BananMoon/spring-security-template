package moon.thinkhard.spring_security_template.authentication.config;

import moon.thinkhard.spring_security_template.authentication.handler.CustomAuthenticationFailureHandler;
import moon.thinkhard.spring_security_template.authentication.handler.CustomAuthenticationSuccessHandler;
import moon.thinkhard.spring_security_template.utils.JwtUtils;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

/**
 * Spring Security 에서 제공하는 폼 로그인을 사용할 경우 22-23 라인을 주석 해제 후 이용하세요.
 */
//@EnableWebSecurity
//@Configuration
public class SecurityConfigWithFormLogin {
    private final JwtUtils jwtUtils;

    public SecurityConfigWithFormLogin(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .headers(header -> header
                        .httpStrictTransportSecurity(hstsConfig -> hstsConfig.maxAgeInSeconds(31536000).includeSubDomains(true).preload(true))
                        .contentSecurityPolicy(cspConfig -> cspConfig.policyDirectives("default-src 'self' object-src 'none' script-src 'none' style-src 'none"))
                        .xssProtection(xssProtection -> xssProtection.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                )
                .sessionManagement(sessionManagementConfig -> sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest.requestMatchers("/loginPage.html", "/error").permitAll()
                                .requestMatchers("/nonMember").hasAuthority("read")
                                .anyRequest().authenticated())
                .formLogin(login ->
                         login
                                 .loginPage("/loginPage.html")
                                 .usernameParameter("id")
                                 .passwordParameter("password")
                                 .loginProcessingUrl("/login")
                                 .successHandler(new CustomAuthenticationSuccessHandler(jwtUtils))
                                 .failureHandler(new CustomAuthenticationFailureHandler())
                                 .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
