package com.icode.api.controller;


import com.icode.api.common.constant.SystemFinal;
import com.icode.api.common.response.tree.DictionaryTreeNode;
import com.icode.api.repository.dto.CmsDictionaryDTO;
import com.icode.api.repository.entity.CmsDictionary;
import com.icode.api.service.ICmsDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title: 数据字典 前端控制器<br>
 * Description: <br>
 * Author: XiaChong<br>
 * Mail: summerpunch@163.com<br>
 * Date: 2019/3/6 9:41<br>
 */
@RestController
@RequestMapping("/api/cms/v1/dictionary")
@Api(value = "数据字典", tags = "数据字典相关接口", description = "用于查询CMS系统中数据字典的相关信息")
public class ApiCmsDictionaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCmsDictionaryController.class);

    @Autowired
    private ICmsDictionaryService service;

    @GetMapping(value = "/get/dictionaryById/{id}", produces = SystemFinal.SYSTEM_PRODUCES_JSON)
    @ApiOperation(value = "按id查询数据字典信息接口", response = CmsDictionaryDTO.class)
    public String gatDictionaryById(@ApiParam(name = "id", value = "id", required = true) @PathVariable("id") Integer id) {
        return service.gatDictionaryById(id);
    }

    @GetMapping(value = "/get/dictionaryListByParentId/{parentId}", produces = SystemFinal.SYSTEM_PRODUCES_JSON)
    @ApiOperation(value = "按ParentId查询子节点接口", response = CmsDictionaryDTO.class)
    public String gatDictionaryListByParentId(@ApiParam(name = "parentId", value = "parentId", required = true) @PathVariable("parentId") Integer parentId) {
        return service.gatDictionaryListByParentId(parentId);
    }

    @GetMapping(value = "/get/treePlugDictionary/{parentId}", produces = SystemFinal.SYSTEM_PRODUCES_JSON)
    @ApiOperation(value = "获取数据字典结构树", response = DictionaryTreeNode.class)
    public String getTreePlugDictionary(@ApiParam(name = "parentId", value = "parentId", defaultValue = "0") @PathVariable("parentId") Integer parentId) {
        return service.getTreePlugDictionary(parentId);
    }

    @GetMapping(value = "/get/treeDictionary/{parentId}", produces = SystemFinal.SYSTEM_PRODUCES_JSON)
    @ApiOperation(value = "获取数据字典结构树", response = DictionaryTreeNode.class)
    public String getTreeDictionary(@ApiParam(name = "parentId", value = "parentId", defaultValue = "0") @PathVariable("parentId") Integer parentId) {
        return service.getTreeDictionary(parentId);
    }

    @GetMapping(value = "/get/AllDictionary", produces = SystemFinal.SYSTEM_PRODUCES_JSON)
    @ApiOperation(value = "获取所有数据字典", response = CmsDictionary.class)
    public String getAllDictionary() {
        return service.getAllDictionary();
    }


}

