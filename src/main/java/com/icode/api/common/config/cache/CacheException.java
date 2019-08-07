package com.icode.api.common.config.cache;

/**
 * Title: 缓存异常相关<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 15:52<br>
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = -5112528854998647834L;

    public CacheException(String s) {
        super(s);
    }

    public CacheException(String s, Throwable e) {
        super(s, e);
    }

    public CacheException(Throwable e) {
        super(e);
    }
}
