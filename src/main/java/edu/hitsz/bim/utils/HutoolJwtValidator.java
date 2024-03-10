package edu.hitsz.bim.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import edu.hitsz.bim.common.AuthenticationConstants;
import edu.hitsz.bim.common.JwtAuthenticationException;

public class HutoolJwtValidator implements JwtValidator {

    @Override
    public String validate(String token) {
        // 验签
        boolean verify = JWTUtil.verify(token, AuthenticationConstants.JWT_KEY.getBytes());
        if (!verify) {
            throw new JwtAuthenticationException("非法令牌");
        }
        // 过期
        final JWT jwt = JWTUtil.parseToken(token);
        long expireTime = Long.parseLong(jwt.getPayload("expire_time").toString());
        if (System.currentTimeMillis() > expireTime) {
            throw new JwtAuthenticationException("令牌已失效");
        }
        // 返回
        return (String) jwt.getPayload("username");
    }
}
