package com.icode.api.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
    private static Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static final String PARAM_DATE_FORMAT = "yyyy-MM-dd";
    public static final String PARAM_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Title: 对指定的时间添加N秒<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 15:26<br>
     */
    public static Date addSecondTime(long time, int second) {
        String timeStr = new Timestamp(time).toString();
        DateFormat df = new SimpleDateFormat(PARAM_DATETIME_FORMAT);
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(df.parse(timeStr));
            c.add(Calendar.SECOND, second);
            return c.getTime();
        } catch (ParseException e) {
            log.error("addSecond,args:{}(time), {}(second) --error:{}", timeStr, second, e);
        }
        return null;
    }


}
