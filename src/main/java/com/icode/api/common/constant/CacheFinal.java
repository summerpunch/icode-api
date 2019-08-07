package com.icode.api.common.constant;

/**
 * Title: 缓存相关常量<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/3/6 9:47<br>
 */
public class CacheFinal {




    /**
     * Title: 预设缓存切分最大值<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 15:18<br>
     */
    public static final Integer PREINSTALL_CACHE_SPLIT_MAX = 20000;

    /**
     * Title: 预设缓存切分值<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 15:18<br>
     */
    public static final Integer PREINSTALL_CACHE_SPLIT_SIZE = 10000;


    /**
     * Title: 预设默认缓存时间<br>
     * Description: 180<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:42<br>
     */
    public static final Integer PREINSTALL_CACHE_DEFAULT_SECONDS_TIME = 180;

    /**
     * Title: 预设默认缓存时间<br>
     * Description: 10<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:42<br>
     */
    public static final Integer PREINSTALL_CACHE_DEFAULT_SECONDS_TIME_TEN = 10;

    /**
     * Title: 新请求锁超时时间默认值-秒<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:43<br>
     */
    public static final int PREINSTALL_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT = 30;

    /**
     * Title: 新请求锁超时重试时间默认值<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:43<br>
     */
    public static final int PREINSTALL_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT_RETRY = 100;

    /**
     * Title: 缓存：缓存超时随机数开始<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:42<br>
     */
    public static final String ENUM_CACHE_SERVICE_RANGE_START = "enum.cache.common.range.begin";

    /**
     * Title: 缓存：缓存超时随机数结束(范围越大越有利于降低缓存压力)<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:42<br>
     */
    public static final String ENUM_CACHE_SERVICE_RANGE_EDN = "enum.cache.common.range.end";

    /**
     * Title: 缓存:新请求锁超时时间<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:43<br>
     */
    public static final String ENUM_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT = "enum.cache.common.newrequestlocktimeout";
    /**
     * Title: 缓存:新请求停顿时间-秒<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:43<br>
     */
    public static final String ENUM_CACHE_COMMON_NEWREQUESTSLEEP = "enum.cache.common.newrequestsleep";
    /**
     * Title: 字典Key默认缓存时间<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:42<br>
     * Param: <br>
     * Return:
     */
    public static final String ENUM_CACHE_DEFAULT_SECONDS_KEY = "enum.cache.common.default";

    /**
     * Title: 服务缓存状态<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 15:13<br>
     */
    public static final String ENUM_CACHE_COMMON_STATUS_API = "enum.cache.common.status.api";

    /**
     * Title: 启用<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 16:00<br>
     */
    public static final String DB_STATUS_ON = "db.status.on";
}
