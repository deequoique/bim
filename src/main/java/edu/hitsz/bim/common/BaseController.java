package edu.hitsz.bim.common;

/**
 * @Description
 * @Date 2022/11/3 10:09
 * @Author Rookie
 */

import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Function;

public abstract class BaseController {


    /**
     * dealWithException
     */
    protected <P, R> Response<R> dealWithException(P param, Function<P, R> func, String logPre) {
        try {
            R rst = func.apply(param);
            return Response.SUCCEED(rst).build();
        } catch (BIMException e) {
            getLog().warn(String.format(logPre + " business exception：%s", e.getMsg()), e);
            return Response.<R>FAILED(e.getCode(), e.getMsg()).build();
        } catch (IllegalArgumentException e) {
            Response.HttpResponseState code = Response.HttpResponseState.PARM_ILLEGAL;
            String message = Optional.ofNullable(e.getMessage()).orElse(code.getDesc());
            getLog().warn(String.format(logPre + " param illegal：%s", message), e);
            return Response.<R>FAILED(code, message).build();
        } catch (Exception e) {
            getLog().warn(logPre + " unknown exception", e);
            return Response.<R>SYSTEM_ERROR().build();
        }
    }

    /**
     * getLog
     */
    protected abstract Logger getLog();

}