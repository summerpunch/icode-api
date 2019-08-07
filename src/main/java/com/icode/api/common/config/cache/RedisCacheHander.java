package com.icode.api.common.config.cache;


import com.icode.api.common.config.exception.GlobalExceptionHandler;
import com.icode.api.common.constant.CacheFinal;
import com.icode.api.common.constant.CustomFinal;
import com.icode.api.common.constant.SystemFinal;
import com.icode.api.common.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Title: 缓存切面<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/8/5 15:55<br>
 */
@Aspect
@Service
public class RedisCacheHander {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisCacheHander.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.icode.api.common.config.cache.ICODECacheable)")
    public void aspect() {}

    /**
     * Title: 将拦截的方法返回的结果存储在缓存中<br>
     * Description:
     * <p>
     * 返回缓存对象，没有则返回拦截的方法返回的对象
     *
     * <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 11:18<br>
     */
    @Around("aspect()&&@annotation(cacheable)")
    public Object interceptor(final ProceedingJoinPoint pjp, ICODECacheable cacheable) throws Throwable {
        if (isCacheByReqArg(pjp)) {
            return pjp.proceed();
        }
        Class returnClass = getReturnClass(pjp);
        if (null == returnClass) {
            return pjp.proceed();
        }
        int cacheTimeOut = getTimeOut(cacheable);
        if (cacheTimeOut == 0) {
            LOGGER.warn("******未配置缓存时间,请检查默认的缓存时间或者业务设定的缓存时间:{}", returnClass);
            return pjp.proceed();
        }
        Object cacheProxy;
        Object cacheObject;
        CacheKey cacheKeyObj = getCacheKey(pjp);
        String cacheKeyName = cacheKeyObj.getKey();
        for (; ; ) {
            cacheProxy = redisTemplate.opsForValue().get(cacheKeyName);
            if (null == cacheProxy) {
                //无缓存
                String lockKey = "icode-api-lock:" + cacheKeyName;
                int fromCacheExpiration = LoadDataUtil.getDicIntValueByKey(CacheFinal.ENUM_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT, CacheFinal.PREINSTALL_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT).intValue();
                long lockValue = System.currentTimeMillis() + fromCacheExpiration + 1;
                if (redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue)) {
                    LOGGER.warn("-------current threads access the db...{} --> {}", cacheKeyName, cacheKeyObj.getKeyOri());
                    redisTemplate.expire(lockKey, fromCacheExpiration, TimeUnit.SECONDS);
                    try {
                        cacheObject = addCache(pjp, redisTemplate, cacheKeyObj, cacheTimeOut);
                        LOGGER.warn("filled the cache!{}", cacheKeyName);
                    } finally {
                        redisTemplate.delete(lockKey);
                    }
                    break;
                } else {
                    Object lockOldValue = redisTemplate.opsForValue().get(lockKey);
                    if (null != lockOldValue) {
                        //超时
                        if (Long.valueOf(lockOldValue.toString()) < System.currentTimeMillis()) {
                            LOGGER.error("-------other thread db lock timeout,has been automatically remove the lock:{} --> {}", cacheKeyName, cacheKeyObj.getKeyOri());
                            redisTemplate.delete(lockKey);
                        }
                    }
                    LOGGER.warn("-------a new thread...{}", cacheKeyName);
                    //新请求休息n毫秒后重试
                    int newRequestSleep = LoadDataUtil.getDicIntValueByKey(CacheFinal.ENUM_CACHE_COMMON_NEWREQUESTSLEEP, CacheFinal.PREINSTALL_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT_RETRY).intValue();
                    Thread.sleep(newRequestSleep);
                    continue;
                }
            } else {
                if (!validateBigData(cacheProxy, redisTemplate, cacheKeyName)) {
                    redisTemplate.delete(cacheKeyName);
                    continue;
                }
                try {
                    cacheObject = cacheYes(pjp, redisTemplate, cacheTimeOut, cacheProxy, cacheKeyObj, cacheKeyName);
                } catch (CompressException e) {
                    redisTemplate.delete(cacheKeyName);
                    continue;
                }
                break;
            }
        }
        return cacheObject;
    }

    private Object cacheYes(ProceedingJoinPoint pjp, RedisTemplate<String, Object> redisTemplate, int cacheTimeOut,
                            Object cacheProxy, CacheKey cacheKeyObj, String cacheKeyName) throws Throwable {
        Object cacheObj;
        if (null != cacheProxy && cacheProxy instanceof CacheObjectProxy) {
            CacheObjectProxy cacheObjectProxy = (CacheObjectProxy) cacheProxy;
            try {
                cacheObj = CompressUtils.decompressObject(cacheObjectProxy.getObject());
            } catch (Exception e) {
                int redisLen = null == cacheObjectProxy.getObject() ? 0 : cacheObjectProxy.getObject().length;
                LOGGER.error("big data decompress problems!key:{},redis size:{},correct:{} -->{}", cacheKeyName, redisLen, cacheObjectProxy.getCount(), e);
                throw new CompressException();
            }
            Date date = DateUtils.addSecondTime(cacheObjectProxy.getTimeStamp(), cacheObjectProxy.getTimeOut());
            //距离缓存过期还有多少秒
            long interval = (date.getTime() - System.currentTimeMillis()) / 1000;

            if (interval < 30) {
                String lockKey = "lock:" + cacheKeyName;
                //新请求超时时间
                int fromCacheExpiration = LoadDataUtil.getDicIntValueByKey(CacheFinal.ENUM_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT, CacheFinal.PREINSTALL_CACHE_COMMON_NEWREQUESTLOCKTIMEOUT).intValue();

                long value = System.currentTimeMillis() + fromCacheExpiration + 1;

                if (redisTemplate.opsForValue().setIfAbsent(lockKey, value)) {
                    LOGGER.warn("populate the cache in advance...{} --> timeout:{}/sec  --> {}", cacheKeyName, interval, cacheKeyObj.getKeyOri());
                    redisTemplate.expire(lockKey, fromCacheExpiration, TimeUnit.SECONDS);
                    try {
                        cacheObj = addCache(pjp, redisTemplate, cacheKeyObj, cacheTimeOut);
                        LOGGER.warn("populate the cache finished ahead of time!{}", cacheKeyName);
                    } finally {
                        redisTemplate.delete(lockKey);
                    }
                } else {
                    LOGGER.info("------hit cache str(fill ing..): {} --> timeout:{}/sec  --> {}", cacheKeyName, interval, cacheKeyObj.getKeyOri());
                }
            } else {
                LOGGER.info("------hit cache obj(direct old): {} --> timeout:{}/sec  --> {}", cacheKeyName, interval, cacheKeyObj.getKeyOri());
            }
        } else if (null != cacheProxy && cacheProxy instanceof String) {
            cacheObj = cacheProxy;
            LOGGER.info("------hit cache no proxy: {} --> {}", cacheKeyName, cacheKeyObj.getKeyOri());
        } else {
            //补偿
            cacheObj = addCache(pjp, redisTemplate, cacheKeyObj, cacheTimeOut);
        }
        return cacheObj;
    }

    private Object addCache(ProceedingJoinPoint pjp, RedisTemplate<String, Object> redisTemplate, CacheKey keyObj, int cacheTimeOut) throws Throwable {
        Object cacheObj;
        try {
            cacheObj = pjp.proceed();
        } catch (Exception e) {
            LOGGER.error("addCache", e);
            cacheObj = GlobalExceptionHandler.serviceExceptionHandler(e);
            cacheTimeOut = CacheFinal.PREINSTALL_CACHE_DEFAULT_SECONDS_TIME;
        }

        /**
         * 这里如果pJoinPoint.proceed()不执行，后面拦截到的方法都不会执行,执行后会退出此方法，继续执行后续的程序
         * 执行目标方法，并保存目标方法执行后的返回值
         *
         * cacheTimeOut += MathUtils.getRandom(LoadData.getDicIntValueByKey(Final.ENUM_CACHE_SERVICE_RANGE_START),
         * LoadData.getDicIntValueByKey(Final.ENUM_CACHE_SERVICE_RANGE_EDN));	//随机数，范围越大，越有利于降低缓存压力
         *
         * 将拦截的方法返回的结果存储在缓存中
         */
        CacheObjectProxy cacheObjectProxy;
        byte[] objBytes = CompressUtils.compressObject(cacheObj);
        /*if (CACHE_SPLIT_ENABLE && objBytes.length >= CacheFinal.PREINSTALL_CACHE_SPLIT_MAX) {
            cacheObjectProxy = new CacheObjectProxy(System.currentTimeMillis(), cacheTimeOut);
            byte[][] bytes = ArrayUtils.splitBytes(objBytes, CacheFinal.PREINSTALL_CACHE_SPLIT_SIZE);
            String keyS = keyObj.getKey() + SystemFinal.SYSTEM_CONNECT_SYMBOL_COLON + cacheObjectProxy.getTimeStamp();
            for (int i = 0; i < bytes.length; i++) {
                redisTemplate.opsForHash().put(keyS, String.valueOf(i), bytes[i]);
            }
            redisTemplate.expire(keyS, cacheTimeOut + CacheFinal.PREINSTALL_CACHE_DEFAULT_SECONDS_TIME_TEN, TimeUnit.SECONDS);
            cacheObjectProxy.setLength(objBytes.length);
            cacheObjectProxy.setCount(bytes.length);
            redisTemplate.opsForValue().set(keyObj.getKey(), cacheObjectProxy, cacheTimeOut, TimeUnit.SECONDS);
        } else {*/
        cacheObjectProxy = new CacheObjectProxy(objBytes, System.currentTimeMillis(), cacheTimeOut);
        redisTemplate.opsForValue().set(keyObj.getKey(), cacheObjectProxy, cacheTimeOut, TimeUnit.SECONDS);
        // }
        LOGGER.info("------put cache: {} --> cache time:{}/sec, {}/b--> {} ", keyObj.getKey(), cacheTimeOut, objBytes.length, keyObj.getKeyOri());
        return cacheObj;
    }

    /**
     * Title: 验证big数据是否正确<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 18:04<br>
     */
    private static boolean validateBigData(Object cacheProxy, RedisTemplate<String, Object> redisTemplate, String cacheKeyName) {
        boolean isOk = true;
        try {
            if ((null != cacheProxy) && (cacheProxy instanceof CacheObjectProxy) && (((CacheObjectProxy) cacheProxy).getCount() > 0)) {
                CacheObjectProxy cacheObjectProxy = (CacheObjectProxy) cacheProxy;
                List<byte[]> list = new LinkedList<>();
                Object ob;
                String cacheKeyNameS = cacheKeyName + SystemFinal.SYSTEM_CONNECT_SYMBOL_COLON + cacheObjectProxy.getTimeStamp();
                for (int i = 0; i < cacheObjectProxy.getCount(); i++) {
                    ob = redisTemplate.opsForHash().get(cacheKeyNameS, String.valueOf(i));
                    if (null == ob) {
                        isOk = false;
                        break;
                    }
                    list.add((byte[]) ob);
                }
                if (isOk) {
                    byte[] meargeObj = null;
                    try {
                        meargeObj = ArrayUtils.mergeArray(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                        isOk = false;
                    }
                    LOGGER.debug("meargeObj size --> {},{}", meargeObj.length, cacheObjectProxy.getLength());
                    if (null == meargeObj || cacheObjectProxy.getLength() != meargeObj.length) {
                        /**
                         * 比对字节数量是否一致，不一致则数据有问题，需重新填充缓存
                         */
                        LOGGER.error("big data size problems!--> {},redis size:{},correct:{}", cacheKeyName, meargeObj.length, cacheObjectProxy.getCount());
                        isOk = false;
                    } else {
                        cacheObjectProxy.setObject(meargeObj);
                    }
                }
            }
        } catch (SerializationException e) {
            isOk = false;
            LOGGER.error("SerializationFailedException --> {},err--{}", cacheKeyName, e);
        }
        return isOk;
    }


    /**
     * Title: 根据拦截的方法生成缓存key<br>
     * Description:
     * <p>
     * 获得缓存key的方法，cache key包括:包名+类名+方法名+各个参数+方法返回对象的uid
     * 示例：如 com.icode.api.service.impl.xxServiceImpl.getxx(listxx)
     *
     * <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 17:43<br>
     */
    private CacheKey getCacheKey(ProceedingJoinPoint pjp) {
        // 拦截实体类名（含包名）
        String className = pjp.getTarget().getClass().getName();
        // 拦截方法名
        String methodName = pjp.getSignature().getName();
        // 拦截方法参数列表
        Object[] arguments = pjp.getArgs();
        // 拦截方法返回对象的uid
        Signature sig = pjp.getSignature();
        Long uid = null;
        if (sig instanceof MethodSignature) {
            MethodSignature msig = (MethodSignature) sig;
            Object target = pjp.getTarget();
            Class<?> cla = null;
            try {
                Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
                cla = currentMethod.getReturnType();
                Field field = cla.getDeclaredField(SystemFinal.SYSTEM_SERIALVERSIONUID);
                field.setAccessible(true);
                uid = field.getLong(cla);
            } catch (Exception e) {
                LOGGER.debug("没有设置uid: {}.{}--》{}", className, methodName, cla);
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append(SystemUtils.isWindows() ? SystemFinal.SYSTEM_WINDOWS : SystemFinal.SYSTEM_LINUX)
                .append(SystemFinal.SYSTEM_CONNECT_SYMBOL_LINE)
                .append(SystemUtils.getProjectEnviroment())
                .append(SystemFinal.SYSTEM_CONNECT_SYMBOL_DOT)
                .append(className)
                .append(SystemFinal.SYSTEM_CONNECT_SYMBOL_COLON)
                .append(methodName);

        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] instanceof String[]) {
                    String[] strArray = (String[]) arguments[i];
                    sb.append(SystemFinal.SYSTEM_CONNECT_SYMBOL_COLON);
                    for (String str : strArray) {
                        sb.append(str);
                    }
                } else {
                    sb.append(SystemFinal.SYSTEM_CONNECT_SYMBOL_COLON).append(arguments[i]);
                }
            }
        }

        if (null != uid) {
            sb.append(uid);
        }
        String keyOri = sb.toString();
        String key = CustomFinal.CUSTOM_ENVIRONMENT_PROFILES_KEY_MD5_PREFIX + MathUtils.encodeMD5(keyOri);
        return new CacheKey(key, keyOri);
    }

    /**
     * Title: 获取超时时间<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 17:31<br>
     */
    private int getTimeOut(ICODECacheable cacheable) {
        //缓存注解自定义的缓存时间,默认是不需要配置,取数据字典配置的
        int customExpire = cacheable.expire();
        String expireKey = cacheable.expireKey();
        if (StringUtils.isNotBlank(expireKey)) {
            //如果注解上设置了缓存时间key
            customExpire = Integer.valueOf(LoadDataUtil.getDicDataByKey(expireKey).getItemValue());
        } else {
            if (customExpire == 0) {
                customExpire = preinstallCacheTime(true);
            }
        }
        customExpire += preinstallCacheTime(false);
        return customExpire;
    }

    /**
     * Title: 获取数据字典默认缓存时间 and 随机缓存时间<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:21<br>
     */
    private int preinstallCacheTime(Boolean flag) {
        if (flag) {
            return LoadDataUtil.getDicIntValueByKey(CacheFinal.ENUM_CACHE_DEFAULT_SECONDS_KEY, CacheFinal.PREINSTALL_CACHE_DEFAULT_SECONDS_TIME).intValue();
        } else {
            return MathUtils.getRandom(LoadDataUtil.getDicIntValueByKey(CacheFinal.ENUM_CACHE_SERVICE_RANGE_START), LoadDataUtil.getDicIntValueByKey(CacheFinal.ENUM_CACHE_SERVICE_RANGE_EDN));
        }
    }


    /**
     * Title: 获取返回类型<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 17:29<br>
     */
    private static Class getReturnClass(ProceedingJoinPoint pjp) {
        Class returnType = null;
        Signature sig = pjp.getSignature();
        if (sig instanceof MethodSignature) {
            MethodSignature msig = (MethodSignature) sig;
            try {
                Method currentMethod = pjp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes());
                Type genericReturnType = currentMethod.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    // 泛型
                    returnType = getListObjectType(genericReturnType);
                } else {
                    // 非泛型
                    Class returnClass = (Class) genericReturnType;
                    boolean isArray = returnClass.isArray();
                    if (isArray) {
                        //返回类型是数组
                        returnType = returnClass.getComponentType();
                    } else {
                        //返回类型是普通对象
                        returnType = currentMethod.getReturnType();
                    }
                }
            } catch (NoSuchMethodException e) {
                LOGGER.error("getReturnClass,err--", e);
            } catch (SecurityException e) {
                LOGGER.error("getReturnClass,err--", e);
            }
            return returnType;
        }
        return null;
    }

    /**
     * 检查是不是不走缓存。true:不走：false:走
     */
    private Boolean isCacheByReqArg(ProceedingJoinPoint pjp) {
        if (LoadDataUtil.getDicIdByKey(CacheFinal.DB_STATUS_ON).equals(LoadDataUtil.getDicDataByKey(CacheFinal.ENUM_CACHE_COMMON_STATUS_API).getStatus())) {
            return false;
        }
        Type returnType = getReturnType(pjp);
        if (null == returnType) {
            return true;
        }
        //返回类型是List
        if (returnType instanceof ParameterizedType) {
            // 泛型
            Class listTypeClass = getListObjectType(returnType);
            boolean isImpl = Serializable.class.isAssignableFrom(listTypeClass);
            if (!isImpl) {
                LOGGER.debug("******缓存失效：返回List内的元素未实现Serializable接口，{}", returnType);
                return true;
            }
        } else {
            // 非泛型
            Class returnClass = (Class) returnType;
            boolean isArray = returnClass.isArray();
            if (isArray) {
                //返回类型是数组
                Class elementType = returnClass.getComponentType();
                if (!Serializable.class.isAssignableFrom(elementType)) {
                    LOGGER.debug("******缓存失效：返回数组内的元素未实现Serializable接口，{}", elementType);
                    return true;
                }
            } else {
                //返回类型是普通对象
                boolean isImpl = Serializable.class.isAssignableFrom(returnClass);
                if (!isImpl) {
                    LOGGER.debug("******缓存失效：返回对象未实现Serializable接口，{}", returnClass);
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Title: 返回对象类型<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:53<br>
     */
    private static Class getListObjectType(Type returnType) {
        Class listTypeClass = null;
        if (ParameterizedType.class.isAssignableFrom(returnType.getClass())) {
            for (Type t1 : ((ParameterizedType) returnType).getActualTypeArguments()) {
                listTypeClass = (Class) t1;
                break;
            }
        }
        return listTypeClass;
    }

    /**
     * Title: 验证返回类型<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/5 16:07<br>
     */
    private static Type getReturnType(ProceedingJoinPoint pjp) {
        Type returnType = null;
        Signature sig = pjp.getSignature();
        if (sig instanceof MethodSignature) {
            MethodSignature msig = (MethodSignature) sig;
            try {
                returnType = pjp.getTarget().getClass().getMethod(msig.getName(), msig.getParameterTypes()).getGenericReturnType();
            } catch (NoSuchMethodException e) {
                LOGGER.error("getReturnType,err--", e);
            } catch (SecurityException e) {
                LOGGER.error("getReturnType,err--", e);
            }
            return returnType;
        }
        return null;
    }
}

