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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUserHandlerService jwtUserHandlerService;
    private final SpgUserService spgUserService;
    private final AuthenticationManager authenticationManager;

    //Constructor needs the JwtUserHandler since it must be passed to the JWT provider in order to add the relationship user/token into the DB
    public CustomAuthenticationFilter(JWTUserHandlerService jwtUserHandlerService, SpgUserService spgUserService, AuthenticationManager authenticationManager) {
        this.jwtUserHandlerService = jwtUserHandlerService;
        this.spgUserService = spgUserService;
        this.authenticationManager = authenticationManager;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        if (!request.getHeader(CONTENT_TYPE).equals(APPLICATION_FORM_URLENCODED_VALUE)) {
//            Map<String, String> responseBody = new HashMap<>();
//            responseBody.put("errorMessage", "Invalid content type");
//            response.setStatus(415);
//            response.setContentType(APPLICATION_JSON_VALUE);
//            new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
//            return null;
//        }
        //get parameters from the request body (x-www-urlencoded type)
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //log the attempt
        log.info("Authentication attempt: " + username);
        //create new UsernameAndPasswordAuthenticationToken object to try the authentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        //Set it into the authentication manager (see security config file)
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
        jwtUserHandlerService.addRelationUserTokens(spgUserService.getUserByEmail(user.getUsername()), responseBody.get("accessToken"), responseBody.get("refreshToken"));
        //set content type and set up the response body
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

}
