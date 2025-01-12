package moon.thinkhard.spring_security_template.config;

import moon.thinkhard.spring_security_template.authentication.CustomAuthenticationFilter;
import moon.thinkhard.spring_security_template.authentication.CustomAuthenticationProvider;
import moon.thinkhard.spring_security_template.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
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
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest.requestMatchers("/loginPage.html", "/login", "/error").permitAll()
                                .requestMatchers("/nonMember").hasAuthority("read")
                                .anyRequest().authenticated())
                .exceptionHandling(handling ->
                        handling
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(new CustomAuthenticationFilter(new ProviderManager(customAuthenticationProvider)), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
