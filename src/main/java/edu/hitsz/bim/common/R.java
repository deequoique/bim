package edu.hitsz.bim.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public static <T> R<T> response(Integer code, String msg, T data) {
        R<T> result = new R<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> R<T> success() {
        return response(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), null);
    }

    public static <T> R<T> fail() {
        return response(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMsg(), null);
    }

    public static <T> R<T> response(ResponseEnum resultCodeEnum,T data) {
        return response(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), data);
    }
}
