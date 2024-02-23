package edu.hitsz.bim.config.security;

import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.common.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseUtils.buildResponse(response, Response.UNAUTHORIZED(), HttpStatus.UNAUTHORIZED);
    }
}
