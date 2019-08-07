package com.icode.api.common.response;

/**
 * Title: 接口响应结果code<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2018/8/13 19:04<br>
 */
public enum ResponseCodeEnum {
    /**
     * Title: 成功<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:05<br>
     */
    SUCCESS(1000, "OK"),
    /**
     * Title: 全局异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:05<br>
     */
    GLOBAL_EXCEPTION(4000, "Global Error"),


    /**
     * Title: 参数校验错误<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:06<br>
     */
    PARAM_INVALID_ERROR(4001, "Param Invalid Error"),

    /**
     * Title: 唯一性校验错误<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:12<br>
     */
    UNIQUENESS_EXCEPTION(4002, "Uniqueness Error"),

    /**
     * Title: 拒绝访问<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:07<br>
     */
    UNAUTHORIZED(4003, "UNAUTHORIZED"),

    /**
     * Title: 找不到资源<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:08<br>
     */
    URL_INVALID_ERROR(4004, "URL Not Found"),

    /**
     * Title: 空指针异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:10<br>
     */
    NULL_EXCEOTION(4005, "Null exception"),

    /**
     * Title: 不允许此方法<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:08<br>
     */
    UNAUTHENTICATEDEXCEPTION(4006, "UnauthenticatedException"),

    /**
     * Title: 被预留错误<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:12<br>
     */
    TYPE_MISMATCH_EXCEPTION(4007, "TypeMismatch exception"),

    /**
     * Title: Service业务错误<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2018/8/13 19:06<br>
     */
    SERVICE_BUSINESS_ERROR(5000, "Service Business Error"),
    /**
     * 运行时异常
     */
    RuntimeException(4012, "isv.runtime_exception"),

    /**
     *
     */
    ClassCastException(4014, "isv.class_cast_exception"),
    /**
     * Title: IO异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 18:23<br>
     */
    IOException(4015, "isv.io_exception"),
    /**
     * Title: 未知方法异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 18:23<br>
     */
    NoSuchMethodException(4016, "isv.no_such_method_exception"),
    /**
     * Title: 数组越界异常<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 18:23<br>
     */
    IndexOutOfBoundsException(4017, "isv.index_out_of_bounds_exception"),

    /**
     * 400错误
     */
    HttpMessageNotReadableException(4018, "isv.http_message_not_readable_exception"),
    /**
     * 500错误
     */
    ConversionNotSupportedException(4022, "isv.conversion_not_supported_exception");

    private int code;
    private String describe;

    ResponseCodeEnum(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
