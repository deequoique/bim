package edu.hitsz.bim.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Return object
 */
@ToString
public class Response<T> implements Serializable {
    @Schema(description = "200 means succeed to call server")
    private Integer code;
    @Schema(description = "response message")
    private String  msg;
    @Schema(description = "boolean means success/fail; or other data object")
    private T       data;

    private Response(){}

    public Response(Builder<T> builder) {
        this.code = builder.code;
        this.msg = builder.message;
        this.data = builder.data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }





    @SuppressWarnings("unchecked")
    public static Response SUCCESS(){
        Response response = new Response();
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMsg(ResponseEnum.SUCCESS.getMsg());
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response SUCCESS(Object data){
        Response response = new Response();
        // boolean
        if (data instanceof Boolean) {
            Map<String, Object> map = new HashMap<>();
            map.put("state", Boolean.parseBoolean(data.toString()) ? 1 : 0);
            response.setData(map);
        }
        // other
        else {
            response.setData(data);
        }
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMsg(ResponseEnum.SUCCESS.getMsg());
        return response;
    }

    @SuppressWarnings("unchecked")
    public static <T> Response SUCCESS(String key, T value){
        Response response = new Response();
        Map<String, T> map = new HashMap<>();
        map.put(key, value);
        response.setData(map);
        response.setCode(ResponseEnum.SUCCESS.getCode());
        response.setMsg(ResponseEnum.SUCCESS.getMsg());
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response ERROR(String msg){
        Response response = new Response();
        response.setCode(ResponseEnum.ERROR.getCode());
        response.setMsg(msg);
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response ERROR(BIMException BIMException){
        Response response = new Response();
        response.setCode(BIMException.getCode());
        response.setMsg(BIMException.getMsg());
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response ERROR(Integer code, String msg){
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response PARA_ERROR(){
        Response response = new Response();
        response.setCode(ResponseEnum.PARAM_ERROR.getCode());
        response.setMsg(ResponseEnum.PARAM_ERROR.getMsg());
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response PARA_ERROR(String message){
        Response response = new Response();
        response.setCode(ResponseEnum.PARAM_ERROR.getCode());
        response.setMsg(message);
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response NOT_FOUND(){
        Response response = new Response();
        response.setCode(ResponseEnum.NOT_FOUND.getCode());
        response.setMsg(ResponseEnum.NOT_FOUND.getMsg());
        return response;
    }

    @SuppressWarnings("unchecked")
    public static Response UNAUTHORIZED(){
        Response response = new Response();
        response.setCode(ResponseEnum.UNAUTHORIZED.getCode());
        response.setMsg(ResponseEnum.UNAUTHORIZED.getMsg());
        return response;
    }


    public static <T> Builder<T> SUCCEED(){
        Builder<T> builder = new Builder<>();
        builder.code = HttpResponseState.SUCCESS.getState();
        builder.message = HttpResponseState.SUCCESS.getDesc();
        return builder;
    }
    public static <T> Builder<T> SYSTEM_ERROR() {
        Builder<T> builder = new Builder<>();
        builder.code = HttpResponseState.SYSTEM_ERROR.getState();
        builder.message = HttpResponseState.SYSTEM_ERROR.getDesc();
        return builder;
    }

    public static <T> Builder<T> SUCCEED(T data) {
        Builder<T> builder = new Builder<>();
        builder.data = data;
        builder.code = HttpResponseState.SUCCESS.getState();
        builder.message = HttpResponseState.SUCCESS.getDesc();
        return builder;
    }

    public static <T> Builder<T> FAILED(String msg) {
        Builder<T> builder = new Builder<>();
        builder.code = HttpResponseState.FAILED.getState();
        builder.message = msg;
        return builder;
    }

    public static <T> Builder<T> FAILED() {
        Builder<T> builder = new Builder<>();
        builder.code = HttpResponseState.FAILED.getState();
        builder.message = HttpResponseState.FAILED.getDesc();
        return builder;
    }

    public static <T> Builder<T> FAILED(HttpResponseState state, String msg) {
        Builder<T> builder = new Builder<>();
        builder.code = state.getState();
        builder.message = msg;
        return builder;
    }

    public static <T> Builder<T> FAILED(Integer code, String msg) {
        Builder<T> builder = new Builder<>();
        builder.code = code;
        builder.message = msg;
        return builder;
    }


    public static class Builder<T> {
        private Integer code;
        private String message;
        private T data;

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Response<T> build() {
            return new Response<>(this);
        }
    }

    @Getter
    public enum HttpResponseState {

        /**
         * 自定义状态码
         */
        SUCCESS(200, "success"),
        FAILED(400, "failed"),
        NO_AUTH(401, "unauthorized"),
        PARM_NULL(402, "param is null"),
        PARM_ILLEGAL(403, "illegal param"),
        SYSTEM_ERROR(500, "system error"),
        UPSTREAM_SYS_ERR(501, "upstream system error"),
        TICKET_EXPIRED(600, "ticket expired")
        ;
        private final int state;
        private final String desc;

        HttpResponseState(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }

        public static HttpResponseState valueOf(int state) {
            for (HttpResponseState codeEnum : values()) {
                if (codeEnum.getState() == state) {
                    return codeEnum;
                }
            }
            return null;
        }

        public int getState() {
            return state;
        }

        public String getDesc() {
            return desc;
        }

    }
}
