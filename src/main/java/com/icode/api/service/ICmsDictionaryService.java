package com.icode.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.icode.api.repository.entity.CmsDictionary;

/**
 * Title: 数据字典 服务类<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/3/6 9:42<br>
 */
public interface ICmsDictionaryService extends IService<CmsDictionary> {

    /**
     * Title: 根据ID查询数据字典<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 11:59<br>
     */
    String gatDictionaryById(Integer id);

    /**
     * Title: 根据parentId查询子节点<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 11:58<br>
     */
    String gatDictionaryListByParentId(Integer parentId);

    /**
     * Title: 获取所有字典数据<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 11:58<br>
     */
    String getAllDictionary();

    /**
     * Title: 获取数据字典结构树<br>
     * Description: 按数据库结构返回<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 11:58<br>
     */
    String getTreeDictionary(Integer parentId);

    /**
     * Title: 获取数据字典结构树<br>
     * Description: 插件使用<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 11:58<br>
     */
    String getTreePlugDictionary(Integer parentId);

}
