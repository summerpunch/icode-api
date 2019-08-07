package com.icode.api.common.utils;

import com.icode.api.common.constant.SystemFinal;

/**
 * Title: 系统帮助类<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 17:49<br>
 */
public class SystemUtils {
    private static String projectEnviroment;


    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(SystemFinal.SYSTEM_WINDOWS)) {
            return true;
        }
        return false;
    }

    public static String getProjectEnviroment() {
        return projectEnviroment;
    }
}
