package com.icode.api.common.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Title: 数学工具<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 17:36<br>
 */
public class MathUtils {

    /**
     * 产生0～max的随机整数 包括0 不包括max
     *
     * @param max 随机数的上限
     * @return
     */
    public static int getRandom(int max) {
        return new Random().nextInt(max);
    }

    /**
     * 产生 min～max的随机整数 包括 min 但不包括 max
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(Integer min, Integer max) {
        if (null != min && null != max) {
            int r = getRandom(max - min);
            return r + min;
        }
        return 0;
    }

    public static String encodeMD5(String data) {
        try {
            return Base64.encode(MessageDigest.getInstance("MD5").digest(data.getBytes()));
        } catch (NoSuchAlgorithmException e) {
        }
        return data;
    }
}
