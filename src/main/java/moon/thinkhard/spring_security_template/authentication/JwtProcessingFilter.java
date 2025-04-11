package moon.thinkhard.spring_security_template.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moon.thinkhard.spring_security_template.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * UsernamePasswordAuthenticationFilter 보다 앞에 위치하는 인증(Authentication) 처리 필터로,
 * JWT 토큰이 유효하면 인증 객체 생성 후 SecurityContextHolder에 세팅한다.
 */
public class JwtProcessingFilter extends OncePerRequestFilter {
    public JwtProcessingFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("JwtProcessingFilter.doFilterInternal() is invoked!");
        Optional<String> optionalJwt = jwtUtils.resolveJwt(request);
         if (optionalJwt.isPresent() && jwtUtils.isValid(optionalJwt.get())) {
            String jwt = optionalJwt.get();

            String username = jwtUtils.getUsername(jwt);
            CustomAuthenticationDetails authenticationDetails = new CustomAuthenticationDetails(request, jwtUtils.getAccountType(jwt));
            Authentication authentication = CustomAuthentication.authenticated(username, authenticationDetails, jwtUtils.getAccountType(jwt).toGrantedAuthorities());

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

//            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
