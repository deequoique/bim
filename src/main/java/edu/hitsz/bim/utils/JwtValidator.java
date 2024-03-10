package edu.hitsz.bim.utils;

import java.io.IOException;

public interface JwtValidator {
    /**
     * 令牌校验，并返回用户唯一标识
     *
     * @param token 令牌字符串
     * @return 用户唯一标识
     */
    String validate(String token) throws IOException;
}
