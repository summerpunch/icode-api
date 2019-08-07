package com.icode.api.common.config.cache;

/**
 * Title: 解压缩异常（自定义）<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 15:52<br>
 */
public class CompressException extends Exception {

    public CompressException() {
        super("compress exception");
    }

    public CompressException(String message) {
        super(message);
    }
}