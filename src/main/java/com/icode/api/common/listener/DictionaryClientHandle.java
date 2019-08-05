package com.icode.api.common.listener;

import com.alibaba.fastjson.JSON;
import com.icode.api.common.utils.LoadDataUtil;
import com.icode.api.repository.entity.CmsDictionary;
import com.icode.api.repository.mapper.CmsDictionaryMapper;
import com.icode.api.service.ICmsDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DictionaryClientHandle {

    private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryClientHandle.class);


    public void loadLocalData(ConfigurableApplicationContext applicationContext, String profile) {
        String data = loadDictionaryFromDB(applicationContext);
        System.out.println(data);
    }


    /**
     * Title: 加载字典进内存<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 15:54<br>
     */
    public String loadDictionaryFromDB(ConfigurableApplicationContext applicationContext) {
        CmsDictionaryMapper mapper = applicationContext.getBean(CmsDictionaryMapper.class);
        List<CmsDictionary> list = LoadDataUtil.initDictionary(mapper);
        if (!list.isEmpty()) {
            return JSON.toJSONString(list);
        }
        return null;
    }


}
