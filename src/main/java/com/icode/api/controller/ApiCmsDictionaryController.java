package com.icode.api.controller;


import com.icode.api.common.response.ResponseData;
import com.icode.api.repository.dto.CmsDictionaryDTO;
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

    private final static String PRODUCES_JSON = "application/json;charset=UTF-8";

    @Autowired
    private ICmsDictionaryService service;

    @GetMapping(value = "/get/dictionaryById/{id}", produces = PRODUCES_JSON)
    @ApiOperation(value = "按id查询数据字典信息接口", response = CmsDictionaryDTO.class)
    public ResponseData gatDictionaryById(@ApiParam(name = "id", value = "id", required = true) @PathVariable("id") Integer id) {
        return service.gatDictionaryById(id);
    }

    @GetMapping(value = "/get/dictionaryListByParentId/{parentId}", produces = PRODUCES_JSON)
    @ApiOperation(value = "按ParentId查询子节点接口", response = CmsDictionaryDTO.class)
    public ResponseData gatDictionaryListByParentId(@ApiParam(name = "parentId", value = "parentId", required = true) @PathVariable("parentId") Integer parentId) {
        return service.gatDictionaryListByParentId(parentId);
    }


}

