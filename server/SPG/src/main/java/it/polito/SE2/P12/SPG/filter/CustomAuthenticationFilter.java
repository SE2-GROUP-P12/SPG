package it.polito.SE2.P12.SPG.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.auth.UserDetailsImpl;
import it.polito.SE2.P12.SPG.service.JWTUserHandlerService;
import it.polito.SE2.P12.SPG.service.SpgUserService;
import it.polito.SE2.P12.SPG.utils.JWTProviderImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUserHandlerService jwtUserHandlerService;
    private final SpgUserService spgUserService;
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(JWTUserHandlerService jwtUserHandlerService, SpgUserService spgUserService, AuthenticationManager authenticationManager) {
        this.jwtUserHandlerService = jwtUserHandlerService;
        this.spgUserService = spgUserService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Authentication attempt: " + username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @SneakyThrows
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //User came from org.springframework.security..., not entity
        //get the userDetails
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        //create the jwt
        JWTProviderImpl jwtProvider = new JWTProviderImpl();
        //map jwt info into response body(creation of the body)
        Map<String, String> responseBody = jwtProvider.getFrontEndUSerJWT(user, request.getRequestURL().toString());
        //Add the relation into the db of jwt and user
        jwtUserHandlerService.addRelationUserTokens(spgUserService.getUserByEmail(user.getUsername()),responseBody.get("accessToken"), responseBody.get("refreshToken"));
        //set content type and set up the response body
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);

    }

}
