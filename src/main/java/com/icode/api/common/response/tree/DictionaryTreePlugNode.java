package com.icode.api.common.response.tree;

import com.icode.api.repository.entity.CmsDictionary;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DictionaryTreePlugNode implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String text;
    private Integer parentId;
    private List<DictionaryTreePlugNode> nodes;


    public void setTreeNode(CmsDictionary cd) {
        if (cd != null) {
            DictionaryTreePlugNode node = new DictionaryTreePlugNode();
            node.setId(cd.getId());
            node.setParentId(cd.getParentId());
            node.setText(cd.getItemNamecn());
            setNode(node);
        }
    }

    private void setNode(DictionaryTreePlugNode node) {
        if (node.getParentId() == 0) {
            this.setId(node.getId());
            this.setParentId(node.getParentId());
            this.setText(node.getText());
        } else {
            this.setNodes(facadeChildNode(this, node));
        }
    }

    /**
     * Title: 递归找到父节点填充数据<br>
     * Description: <br>
     * Author: XiaChong<br>
     * Mail: summerpunch@163.com<br>
     * Date: 2019/3/8 14:45<br>
     */
    private List<DictionaryTreePlugNode> facadeChildNode(DictionaryTreePlugNode thisNodes, DictionaryTreePlugNode node) {
        if (thisNodes.getId().equals(node.getParentId())) {
            List<DictionaryTreePlugNode> nodes = thisNodes.getNodes();
            if (null == nodes) {
                nodes = new ArrayList<>();
            }
            nodes.add(node);
            thisNodes.setNodes(nodes);
        } else {
            List<DictionaryTreePlugNode> nodes = thisNodes.getNodes();
            if (null != nodes) {
                for (DictionaryTreePlugNode data : nodes) {
                    this.setNodes(facadeChildNode(data, node));
                }
            }
        }
        return this.getNodes();
    }
}
