package edu.hitsz.bim.common;



/**
 * exception object
 */
public class BIMException extends  RuntimeException{
    private Integer code;
    private String  msg;

    private BIMException(){};

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

    public static BIMException build(Integer code, String msg) {
        BIMException BIMException = new BIMException();
        BIMException.setCode(code);
        BIMException.setMsg(msg);
        // error log
        if (ResponseEnum.ERROR.getCode().equals(code)) {
            LogUtil.error(msg);
        }
        return BIMException;
    }

    public static BIMException build(ResponseEnum responseEnum) {
        return build(responseEnum.getCode(), responseEnum.getMsg());
    }

    public static BIMException build(ResponseEnum responseEnum, String message) {
        return build(responseEnum.getCode(), message);
    }
}
