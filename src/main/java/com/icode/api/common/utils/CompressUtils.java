package com.icode.api.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * Title: 压缩帮助类<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 18:15<br>
 */
public class CompressUtils {
    private static Logger log = LoggerFactory.getLogger(CompressUtils.class);

    public static byte[] compressObject(Object proxy) {
        JdkSerializationRedisSerializer jdkSer = new JdkSerializationRedisSerializer();
        byte[] bt = null;
        try {
            byte[] sr = jdkSer.serialize(proxy);
            bt = Snappy.compress(sr);
            log.info("------source:{} > {} /b", sr.length, bt.length);
            sr = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bt;
    }

    public static Object decompressObject(byte[] bytes) throws Exception {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        Object cop = null;
        try {
            byte[] f = Snappy.uncompress(bytes);
            JdkSerializationRedisSerializer jdkSer = new JdkSerializationRedisSerializer();
            cop = jdkSer.deserialize(f);
        } catch (Exception e) {
            throw e;
        }
        return cop;
    }

    public static byte[] compressStr(String str) {
        try {
            return Snappy.compress(str.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompressStr(byte[] bytes) {
        try {
            return new String(Snappy.uncompress(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
