package com.example.clickhousequery.condition.logic;

import com.example.clickhousequery.condition.TreeNode;
import com.example.clickhousequery.condition.compare.CompareTreeNode;
import com.example.clickhousequery.entity.DataModel;
import com.example.clickhousequery.serializer.LogicalOperatorDeserializer;
import com.example.clickhousequery.serializer.LogicalOperatorSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class LogicTreeNode implements TreeNode {

    @JsonDeserialize(using = LogicalOperatorDeserializer.class)
    @JsonSerialize(using = LogicalOperatorSerializer.class)
    private LogicalOperator operator; // 运算符，可以是 AND/OR 等
    private List<TreeNode> children; // 子节点列表

    public LogicTreeNode(LogicalOperator operator, List<TreeNode> children){
        this.operator = operator;
        this.children = children;
    }

    public LogicTreeNode(LogicalOperator operator) {
        this.operator = operator;
        this.children = new ArrayList<>();
    }

    public static LogicTreeNode buildWithOp(LogicalOperator operator) {
        return new LogicTreeNode(operator);
    }

    public LogicTreeNode addChild(TreeNode child) {
        this.children.add(child);
        return this;
    }

    @Override
    public void check() {
        if (CollectionUtils.isEmpty(children)) {
            log.error("children node is empty");
            throw new RuntimeException("children node is empty");
        }
        children.forEach(TreeNode::check);
    }

    public String toQuery() {
        StringBuilder sb = new StringBuilder();
        for (TreeNode n : children) {
            if (n instanceof CompareTreeNode) {
                sb.append(n.toQuery()).append(" ").append(operator.getSymbol()).append(" ");
            } else {
                sb.append("(").append(n.toQuery()).append(")").append(" ").append(operator.getSymbol()).append(" ");
            }
        }
        sb.delete(sb.length() - operator.getSymbol().length() - 1, sb.length());
        return sb.toString();
    }

    @Override
    public void fieldToColumn(DataModel model) {
        children.forEach(node -> node.fieldToColumn(model));
    }
}
