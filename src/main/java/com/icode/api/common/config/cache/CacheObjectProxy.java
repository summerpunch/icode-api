package com.icode.api.common.config.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * Title: 缓存代理<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 15:50<br>
 */
@Data
public class CacheObjectProxy implements Serializable {

    private static final long serialVersionUID = 4514245335881460108L;

    private byte[] object;
    /**
     * 时间戳：添加缓存的时间（何时添加）
     */
    private long timeStamp;

    /**
     * 缓存超时时间
     */
    private int timeOut;

    /**
     * 切分大小
     */
    private int count;

    /**
     * 总大小
     */
    private int length;

    public CacheObjectProxy() {
    }

    public CacheObjectProxy(long timeStamp, int timeOut) {
        this(null, timeStamp, timeOut);
    }

    public CacheObjectProxy(byte[] object, long timeStamp, int timeOut) {
        this.object = object;
        this.timeStamp = timeStamp;
        this.timeOut = timeOut;
    }

}