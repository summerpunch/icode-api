package com.icode.api.repository.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CmsDictionaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id",value = "id")
    private Integer id;
    /**
     * 父节点 id
     */
    @ApiModelProperty(name = "parentId",value = "父节点 id")
    private Integer parentId;
    /**
     * 字典key：枚举形式的唯一key
     */
    @ApiModelProperty(name = "itemKey",value = "字典key：枚举形式的唯一key")
    private String itemKey;
    /**
     * 字典值：枚举key对应的value，此值并不能作为外键用，一般用于配置项对应的值，而表关联则用字典主键id，
     */
    @ApiModelProperty(name = "itemValue",value = "字典值：枚举key对应的value，此值并不能作为外键用，一般用于配置项对应的值，而表关联则用字典主键id")
    private String itemValue;
    /**
     * 字典中文名称
     */
    @ApiModelProperty(name = "itemNamecn",value = "字典中文名称")
    private String itemNamecn;
    /**
     * 顺序
     */
    @ApiModelProperty(name = "sort",value = "顺序")
    private Integer sort;
    /**
     * 状态：启用、禁用（废弃）
     */
    @ApiModelProperty(name = "status",value = "状态：启用、禁用（废弃）")
    private String status;
    /**
     * 备注：具体用途细节等
     */
    @ApiModelProperty(name = "description",value = "备注：具体用途细节等")
    private String description;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime",value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "updateTime",value = "更新时间")
    private Date updateTime;
    /**
     * 创建人员
     */
    @ApiModelProperty(name = "adminCreate",value = "创建人员")
    private Integer adminCreate;
    /**
     * 更新人员
     */
    @ApiModelProperty(name = "adminUpdate",value = "更新人员")
    private Integer adminUpdate;

}
