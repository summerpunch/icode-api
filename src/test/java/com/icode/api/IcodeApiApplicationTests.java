package com.icode.api;

import com.icode.api.repository.entity.CmsDictionary;
import com.icode.api.repository.mapper.CmsDictionaryMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IcodeApiApplicationTests {

    @Autowired
    private CmsDictionaryMapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
        ////ResponseData data = service.getDictionaryTree();
        //	System.out.println(data);

        CmsDictionary cmsDictionary = mapper.selectById(4);

        redisTemplate.opsForValue().set("f", cmsDictionary);
       // redisTemplate.delete("f");

    }

}
