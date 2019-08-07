package com.icode.api.common.utils;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    /**
     * Title: 合并字节数组<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 18:06<br>
     */
    public static byte[] mergeArray(List<byte[]> a) {
        // 合并完之后数组的总长度
        int index = 0;
        int sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum = sum + a.get(i).length;
        }
        byte[] result = new byte[sum];
        for (int i = 0; i < a.size(); i++) {
            int lengthOne = a.get(i).length;
            if (lengthOne == 0) {
                continue;
            }
            // 拷贝数组
            System.arraycopy(a.get(i), 0, result, index, lengthOne);
            index = index + lengthOne;
        }
        return result;
    }

    /**
     * Title: 拆分byte数组<br>
     * Description:
     * <p>
     * bytes 要拆分的数组
     * copies 要按几个组成一份
     *
     * <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 19:27<br>
     */
    public static byte[][] splitBytes(byte[] bytes, int copies) {
        double split_length = Double.parseDouble(copies + "");
        int array_length = (int) Math.ceil(bytes.length / split_length);
        byte[][] result = new byte[array_length][];
        int from, to;
        for (int i = 0; i < array_length; i++) {
            from = (int) (i * split_length);
            to = (int) (from + split_length);
            if (to > bytes.length)
                to = bytes.length;
            result[i] = Arrays.copyOfRange(bytes, from, to);
        }
        return result;
    }

}
