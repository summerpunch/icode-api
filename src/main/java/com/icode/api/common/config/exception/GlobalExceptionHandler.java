package com.icode.api.common.config.exception;

import com.alibaba.fastjson.JSON;
import com.icode.api.common.response.ResponseCodeEnum;
import com.icode.api.common.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;


/**
 * Title: 全局异常捕获<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/2/27 17:59<br>
 */
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Title: 空指针异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/27 17:44<br>
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseData nullPointerExceptionHandler(NullPointerException e) {
        LOGGER.error("nullPointerExceptionHandler", e);
        ResponseData responseData = new ResponseData();
        responseData.setCode(ResponseCodeEnum.NULL_EXCEOTION.getCode());
        responseData.setMsg(e.getMessage());
        responseData.setSuccess(false);
        return responseData;
    }

    /**
     * Title: 方法参数校验异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/27 17:44<br>
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseData methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOGGER.error("methodArgumentTypeMismatchException", e);
        ResponseData responseData = new ResponseData();
        responseData.setCode(ResponseCodeEnum.TYPE_MISMATCH_EXCEPTION.getCode());
        responseData.setMsg(e.getMessage());
        responseData.setSuccess(false);
        return responseData;
    }

    /**
     * Title: 其他异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/27 17:45<br>
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData allExceptionHandler(Exception e) {
        LOGGER.error("allExceptionHandler", e);
        e.printStackTrace();
        ResponseData responseData = new ResponseData();
        responseData.setCode(ResponseCodeEnum.GLOBAL_EXCEPTION.getCode());
        responseData.setMsg("GlobalExceptionHandler.Exception");
        responseData.setSuccess(false);
        return responseData;
    }

    public static String serviceExceptionHandler(Exception ex) {
        LOGGER.error("====serviceExceptionHandler", ex);
        ResponseData responseData = new ResponseData();
        responseData.setMsg(ex.getMessage());
        responseData.setSuccess(false);
        if (ex instanceof NullPointerException) {
            responseData.setCode(ResponseCodeEnum.NULL_EXCEOTION.getCode());
        } else if (ex instanceof ClassCastException) {
            responseData.setCode(ResponseCodeEnum.ClassCastException.getCode());
        } else if (ex instanceof IOException) {
            responseData.setCode(ResponseCodeEnum.IOException.getCode());
        } else if (ex instanceof NoSuchMethodException) {
            responseData.setCode(ResponseCodeEnum.NoSuchMethodException.getCode());
        } else if (ex instanceof IndexOutOfBoundsException) {
            responseData.setCode(ResponseCodeEnum.IndexOutOfBoundsException.getCode());
        } else if (ex instanceof HttpMessageNotReadableException) {
            responseData.setCode(ResponseCodeEnum.HttpMessageNotReadableException.getCode());
        } else if (ex instanceof ConversionNotSupportedException) {
            responseData.setCode(ResponseCodeEnum.ConversionNotSupportedException.getCode());
        } else if (ex instanceof RuntimeException) {
            responseData.setCode(ResponseCodeEnum.RuntimeException.getCode());
        } else if (ex instanceof Exception) {
            responseData.setCode(ResponseCodeEnum.SERVICE_BUSINESS_ERROR.getCode());
        }
        return JSON.toJSONString(responseData);
    }
}
