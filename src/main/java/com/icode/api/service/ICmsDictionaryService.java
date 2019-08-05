package com.icode.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.icode.api.common.response.ResponseData;
import com.icode.api.repository.entity.CmsDictionary;

/**
 * Title: 数据字典 服务类<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/3/6 9:42<br>
 */
public interface ICmsDictionaryService extends IService<CmsDictionary> {


    ResponseData gatDictionaryById(Integer id);

    ResponseData gatDictionaryListByParentId(Integer parentId);
}
