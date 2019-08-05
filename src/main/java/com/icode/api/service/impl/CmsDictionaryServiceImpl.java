package com.icode.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icode.api.common.constant.DictFinal;
import com.icode.api.common.response.ResponseData;
import com.icode.api.common.response.ResponseUtil;
import com.icode.api.repository.dto.CmsDictionaryDTO;
import com.icode.api.repository.entity.CmsDictionary;
import com.icode.api.repository.mapper.CmsDictionaryMapper;
import com.icode.api.service.ICmsDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Title: 数据字典 服务实现类<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/3/6 9:53<br>
 */
@Service
public class CmsDictionaryServiceImpl extends ServiceImpl<CmsDictionaryMapper, CmsDictionary> implements ICmsDictionaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsDictionaryServiceImpl.class);

    @Autowired
    private CmsDictionaryMapper mapper;

    @Override
    @Cacheable(cacheNames = "gatDictionaryById")
    public ResponseData gatDictionaryById(Integer id) {
        CmsDictionary cmsDictionary = mapper.selectById(id);
        CmsDictionaryDTO dto = new CmsDictionaryDTO();
        BeanUtils.copyProperties(cmsDictionary, dto);
        return ResponseUtil.success(dto);
    }

    @Override
    @Cacheable(cacheNames = "gatDictionaryListByParentId")
    public ResponseData gatDictionaryListByParentId(Integer parentId) {
        QueryWrapper<CmsDictionary> wrapper = new QueryWrapper<>();
        wrapper.eq(DictFinal.DICT_COLUMN_PARENT_ID, parentId);
        List<CmsDictionary> cmsDictionaries = mapper.selectList(wrapper);
        return ResponseUtil.success(cmsDictionaries);
    }

}
