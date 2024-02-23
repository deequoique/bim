package edu.hitsz.bim.config.security;

import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.common.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        ResponseUtils.buildResponse(response, Response.UNAUTHORIZED(), HttpStatus.UNAUTHORIZED);
    }
}
