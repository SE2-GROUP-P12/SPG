package it.polito.SE2.P12.SPG.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.SE2.P12.SPG.utils.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(403);
        ApiResponse response = new ApiResponse(403, "Access Denied");
        response.setMessage("Access Denied");
        OutputStream out = httpResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
    
}
