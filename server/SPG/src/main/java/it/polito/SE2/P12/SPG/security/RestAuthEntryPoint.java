package it.polito.SE2.P12.SPG.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class RestAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationException authException) throws IOException, ServletException {
        if (httpResponse.getStatus() == FORBIDDEN.value()){
            httpResponse.setStatus(403);
        }
        else{
            httpResponse.setStatus(401);
        }
    }
}
