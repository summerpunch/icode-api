package com.icode.api.common.config.cache;

import com.icode.api.common.constant.CustomFinal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Title: 自定义注解用于缓存<br>
 * Description: 对于使用此注解的方法，会设置缓存<br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 15:44<br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface ICODECacheable {

    String region() default CustomFinal.CUSTOM_CUSTOM_PROJECT_CACHE;

    /**
     * Title: 默认使用包名+方法名+参数<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 15:44<br>
     */
    String key() default "";


    /**
     * Title: 过期时间<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 15:44<br>
     */
    int expire() default 0;

    /**
     * Title: 优先字典配置的缓存<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 15:44<br>
     */
    String expireKey() default "";

}