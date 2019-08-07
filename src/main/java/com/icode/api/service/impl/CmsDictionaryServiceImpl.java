package com.icode.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icode.api.common.response.ResponseUtil;
import com.icode.api.common.utils.LoadDataUtil;
import com.icode.api.repository.entity.CmsDictionary;
import com.icode.api.repository.mapper.CmsDictionaryMapper;
import com.icode.api.service.ICmsDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String gatDictionaryById(Integer id) {
        return ResponseUtil.toSuccessJSON(LoadDataUtil.getDicDataById(id));
    }

    @Override
    public String gatDictionaryListByParentId(Integer parentId) {
        return ResponseUtil.toSuccessJSON(LoadDataUtil.getDicChildById(parentId));
    }

    @Override
    public String getAllDictionary() {
        return ResponseUtil.toSuccessJSON(LoadDataUtil.getAllDictionary());
    }

    @Override
    public String getTreePlugDictionary(Integer parentId) {
        return ResponseUtil.toSuccessJSON(LoadDataUtil.getTreePlugDictionary(parentId));
    }

    @Override
    public String getTreeDictionary(Integer parentId) {
        return ResponseUtil.toSuccessJSON(LoadDataUtil.getTreeDictionary(parentId));
    }

}
