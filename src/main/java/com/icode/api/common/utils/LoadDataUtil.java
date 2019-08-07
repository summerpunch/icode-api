package com.icode.api.common.utils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icode.api.common.constant.DictFinal;
import com.icode.api.common.response.tree.DictionaryTreeNode;
import com.icode.api.common.response.tree.DictionaryTreePlugNode;
import com.icode.api.repository.entity.CmsDictionary;
import com.icode.api.service.ICmsDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Title: 加载内存数据字典<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/2/28 17:25<br>
 */
public class LoadDataUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadDataUtil.class);

    /**
     * Title: 字典Map<br>
     * Description: Key为字典Key<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:30<br>
     */
    private static Map<String, CmsDictionary> keyMap = new HashMap<>();

    /**
     * Title: 字典Map<br>
     * Description: Key为字典ID<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:31<br>
     */
    private static Map<Integer, CmsDictionary> idMap = new HashMap<>();

    /**
     * Title: 字典Map<br>
     * Description: Key为字典父ID,值为所属子节点<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:31<br>
     */
    private static Map<Integer, List<CmsDictionary>> mapChilds = new HashMap<>();

    /**
     * Title: 字典集合<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:31<br>
     */
    private static List<CmsDictionary> listNodes = new ArrayList<>();

    /**
     * Title: 字典树<br>
     * Description: 插件使用<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:41<br>
     */
    private static DictionaryTreePlugNode rootPlugTree = new DictionaryTreePlugNode();

    /**
     * Title: 字典树<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:41<br>
     */
    private static DictionaryTreeNode rootTree = new DictionaryTreeNode();

    /**
     * Title: 读写锁<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:31<br>
     */
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    /**
     * Title: 读锁<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 9:47<br>
     */
    private static Lock r = rwl.readLock();

    /**
     * Title: 写锁<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 9:47<br>
     */
    private static Lock w = rwl.writeLock();


    /**
     * Title: 加载字典进内存<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 15:50<br>
     */
    public static void buildLocalCache(List<CmsDictionary> listDictionary) {
        w.lock();
        try {
            if (!listDictionary.isEmpty()) {
                clearMap();
                listDictionary.parallelStream().forEachOrdered(cd -> {

                            keyMap.put(cd.getItemKey(), cd);

                            idMap.put(cd.getId(), cd);

                            List<CmsDictionary> nodes = mapChilds.get(cd.getParentId());
                            if (CollectionUtils.isEmpty(nodes)) {
                                nodes = new ArrayList<>();
                            }
                            nodes.add(cd);
                            mapChilds.put(cd.getParentId(), nodes);
                        }
                );
                listNodes = listDictionary;
            }
        } finally {
            w.unlock();
        }
    }


    /**
     * Title: 转换为tree对象<br>
     * Description: 插件使用<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:56<br>
     */
    public static DictionaryTreePlugNode assembleDataPlug(CmsDictionary cd) {
        DictionaryTreePlugNode node = new DictionaryTreePlugNode();
        node.setId(cd.getId());
        node.setParentId(cd.getParentId());
        node.setText(cd.getItemNamecn());
        return node;
    }

    /**
     * Title: 转换为tree对象<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:56<br>
     */
    public static DictionaryTreeNode assembleData(CmsDictionary cd) {
        DictionaryTreeNode node = new DictionaryTreeNode();
        BeanUtils.copyProperties(cd, node);
        return node;
    }

    /**
     * Title: 组装为tree结构对象<br>
     * Description: 插件使用<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:56<br>
     */
    private static void assembleTreePlug(Integer parentId) {
        List<CmsDictionary> roots = mapChilds.get(parentId);
        if (CollectionUtils.isEmpty(roots)) {
            return;
        }

        if (parentId.equals(rootPlugTree.getParentId())) {
            return;
        }

        rootPlugTree = assembleDataPlug(roots.get(0));
        recursionNodeTreePlug(rootPlugTree);
    }

    /**
     * Title: 组装为tree结构对象<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 17:56<br>
     */
    private static void assembleTree(Integer parentId) {
        List<CmsDictionary> roots = mapChilds.get(parentId);
        if (CollectionUtils.isEmpty(roots)) {
            return;
        }

        if (parentId.equals(rootTree.getParentId())) {
            return;
        }

        BeanUtils.copyProperties(roots.get(0), rootTree);
        recursionNodeTree(rootTree);
    }

    /**
     * Title: 递归调用<br>
     * Description:
     * <p>
     * 在字典map中查找所属子节点
     * <p>
     * 插件使用
     * <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 11:05<br>
     */
    private static void recursionNodeTreePlug(DictionaryTreePlugNode rootTree) {
        List<CmsDictionary> dictionaries = mapChilds.get(rootTree.getId());
        if (!CollectionUtils.isEmpty(dictionaries)) {
            List<DictionaryTreePlugNode> childs = new ArrayList<>();
            for (CmsDictionary cd : dictionaries) {
                DictionaryTreePlugNode node = assembleDataPlug(cd);
                recursionNodeTreePlug(node);
                childs.add(node);
            }
            rootTree.setNodes(childs);
        }
    }

    /**
     * Title: 递归调用<br>
     * Description:
     * <p>
     * 在字典map中查找所属子节点
     * <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/7 11:05<br>
     */
    private static void recursionNodeTree(DictionaryTreeNode rootTree) {
        List<CmsDictionary> dictionaries = mapChilds.get(rootTree.getId());
        if (!CollectionUtils.isEmpty(dictionaries)) {
            List<DictionaryTreeNode> childs = new ArrayList<>();
            for (CmsDictionary cd : dictionaries) {
                DictionaryTreeNode node = assembleData(cd);
                recursionNodeTree(node);
                childs.add(node);
            }
            rootTree.setNodes(childs);
        }
    }

    /**
     * Title: 清空数据<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 15:52<br>
     */
    private static void clearMap() {
        if (!CollectionUtils.isEmpty(idMap)) {
            idMap.clear();
        }
        if (!CollectionUtils.isEmpty(keyMap)) {
            keyMap.clear();
        }
        if (!CollectionUtils.isEmpty(listNodes)) {
            listNodes.clear();
        }
    }

    /**
     * Title: 获取所有字典数据<br>
     * Description: 返回List<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:42<br>
     */
    public static List<CmsDictionary> getAllDictionary() {
        return listNodes;
    }

    /**
     * Title: 获取字典数据结构树<br>
     * Description:
     * <p>
     * 如果传入0，则从根节点返回
     * <p>
     * 如果不是0，则从指定节点开始返回
     * <p>
     * 返回List<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:42<br>
     */
    public static DictionaryTreePlugNode getTreePlugDictionary(Integer parentId) {
        assembleTreePlug(parentId);
        return rootPlugTree;
    }

    /**
     * Title: 获取字典数据结构树<br>
     * Description:
     * <p>
     * 根据传入的ID ，则从指定节点开始返回
     * <p>
     * 返回List<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:42<br>
     */
    public static DictionaryTreeNode getTreeDictionary(Integer parentId) {
        assembleTree(parentId);
        return rootTree;
    }

    /**
     * Title: 根据key获取字典id<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:42<br>
     */
    public static Integer getDicIdByKey(String key) {
        r.lock();
        try {
            CmsDictionary dictionary = getDicDataByKey(key);
            if (dictionary == null) {
                return null;
            }
            return dictionary.getId();
        } finally {
            r.unlock();
        }
    }

    /**
     * Title: 根据id获取字典name<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:42<br>
     */
    public static String getDicItemNamecnByKey(Integer id) {
        r.lock();
        try {
            CmsDictionary dictionary = getDicDataById(id);
            if (dictionary == null) {
                return null;
            }
            return dictionary.getItemNamecn();
        } finally {
            r.unlock();
        }
    }

    /**
     * Title: 根据key获取字典数据<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:42<br>
     */
    public static CmsDictionary getDicDataByKey(String key) {
        CmsDictionary vo = null;
        if (StringUtils.isNotBlank(key)) {
            vo = keyMap.get(key);
            if (null == vo) {
                LOGGER.error("--------------no dictionary-----------{}", key);
            }
        }
        return vo;
    }

    /**
     * Title: 根据id获取字典数据<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:42<br>
     */
    public static CmsDictionary getDicDataById(Integer id) {
        CmsDictionary vo = null;
        if (null != id) {
            vo = idMap.get(id);
            if (null == vo) {
                LOGGER.error("--------------no dictionary-----------{}", id);
            }
        }
        return vo;
    }

    /**
     * Title: 根据key获取子节点<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:52<br>
     */
    public static List<CmsDictionary> getDicChildByKey(String key) {
        r.lock();
        try {
            CmsDictionary dicDataByKey = getDicDataByKey(key);
            if (dicDataByKey == null) {
                return null;
            }
            return getDicChild(dicDataByKey);
        } finally {
            r.unlock();
        }
    }

    /**
     * Title: 根据Id获取子节点<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:52<br>
     */
    public static List<CmsDictionary> getDicChildById(Integer id) {
        r.lock();
        try {
            CmsDictionary cmsDictionary = getDicDataById(id);
            if (cmsDictionary == null) {
                return null;
            }
            return getDicChild(cmsDictionary);
        } finally {
            r.unlock();
        }
    }

    /**
     * Title: 查找子节点<br>
     * Description: 根据父节点查找子节点<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/3/1 10:18<br>
     */
    public static List<CmsDictionary> getDicChild(CmsDictionary cmsDictionary) {
        return mapChilds.get(cmsDictionary.getId());
    }

    /**
     * Title: 初始化字典数据<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/2/28 16:20<br>
     */
    public static List<CmsDictionary> initDictionary(ICmsDictionaryService service) {
        QueryWrapper<CmsDictionary> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc(DictFinal.DICT_COLUMN_ITEM_LEVEL);
        wrapper.orderByAsc(DictFinal.DICT_COLUMN_SORT);
        List<CmsDictionary> listNodes = service.list(wrapper);
        if (CollectionUtils.isEmpty(listNodes)) {
            return null;
        }
        LoadDataUtil.buildLocalCache(listNodes);
        return listNodes;
    }


    /**
     * Title: 根据key获取字典value<br>
     * Description:
     *
     * <p>
     * 如果字典key没有value
     * <p>
     * 则返回默认值
     *
     * <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:08<br>
     */
    public static Integer getDicIntValueByKey(String key, int defaultValue) {
        r.lock();
        try {
            CmsDictionary cmsDictionary = getDicDataByKey(key);
            return StringUtils.isNotBlank(cmsDictionary.getItemValue()) ? Integer.valueOf(cmsDictionary.getItemValue()) : defaultValue;
        } finally {
            r.unlock();
        }
    }

    /**
     * Title: 根据key获取字典value<br>
     * Description:<br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/8/6 14:08<br>
     */
    public static Integer getDicIntValueByKey(String key) {
        r.lock();
        try {
            CmsDictionary cmsDictionary = getDicDataByKey(key);
            return Integer.valueOf(cmsDictionary.getItemValue());
        } finally {
            r.unlock();
        }
    }


}
