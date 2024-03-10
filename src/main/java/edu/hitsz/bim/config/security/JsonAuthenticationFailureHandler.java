package edu.hitsz.bim.config.security;

import edu.hitsz.bim.common.R;
import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.common.ResponseEnum;
import edu.hitsz.bim.common.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("log failed.");
        ResponseUtils.buildResponse(response, R.response(ResponseEnum.UNAUTHORIZED,null), HttpStatus.UNAUTHORIZED);
    }
}
