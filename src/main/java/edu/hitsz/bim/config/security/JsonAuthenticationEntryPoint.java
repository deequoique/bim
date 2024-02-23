package edu.hitsz.bim.config.security;

import edu.hitsz.bim.common.R;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.common.ResponseEnum;
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
        ResponseUtils.buildResponse(response, R.response(ResponseEnum.UNAUTHORIZED,null), HttpStatus.UNAUTHORIZED);
    }
}
