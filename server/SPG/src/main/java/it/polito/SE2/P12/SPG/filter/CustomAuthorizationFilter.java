package it.polito.SE2.P12.SPG.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.service.JWTUserHandlerService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.JWTProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JWTUserHandlerService jwtUserHandlerService;
    private final SpgUserService spgUserService;

    public CustomAuthorizationFilter(JWTUserHandlerService jwtUserHandlerService, SpgUserService spgUserService) {
        this.jwtUserHandlerService = jwtUserHandlerService;
        this.spgUserService = spgUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                JWTProviderImpl jwtProvider = new JWTProviderImpl();
                try {
                    String username = jwtProvider.verifyAccessToken(token);
                    jwtUserHandlerService.checkTokenValidity(spgUserService.getUserByEmail(username), token);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.error("Error logging in: " + e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), jwtProvider.setErrorMessage(e));
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
