package edu.hitsz.bim.config.security;

import edu.hitsz.bim.common.Response;
import edu.hitsz.bim.common.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * @param request        请求
     * @param response       响应
     * @param authentication 成功认证的用户信息
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ResponseUtils.buildResponse(response, Response.SUCCEED(), HttpStatus.OK);
    }
}
