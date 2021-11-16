package it.polito.SE2.P12.SPG.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AuthenticationException authException) throws IOException, ServletException {
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.sendError(401, "Unauthorized, please try to login with authorized user.");
    }
}
