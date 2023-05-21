package com.example.clickhousequery.condition.compare;

import com.example.clickhousequery.condition.TreeNode;
import com.example.clickhousequery.entity.DataModel;
import com.example.clickhousequery.serializer.ComparisonOperatorDeserializer;
import com.example.clickhousequery.serializer.ComparisonOperatorSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Getter
@Setter
public class CompareTreeNode implements TreeNode {

    private static final String simple = "`%s`%s'%s'";
    private static final String between = "`%s` BETWEEN '%s' AND '%s'";
    private static final String in = "`%s` IN [%s]";

    @JsonDeserialize(using = ComparisonOperatorDeserializer.class)
    @JsonSerialize(using = ComparisonOperatorSerializer.class)
    private ComparisonOperator operator; // 比较运算符，例如 =/>/< 等
    private String leftOperand; // 左操作数
    private List<String> rightOperands; // 右操作数列表

    public CompareTreeNode(ComparisonOperator operator, String leftOperand, List<String> rightOperands) {
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperands = rightOperands;
    }

    @Override
    public void check() {
        if (leftOperand == null) {
            log.error("lack of left operand: {}", this);
            throw new RuntimeException("lack of left operand");
        }
        if (CollectionUtils.isEmpty(rightOperands)) {
            log.error("lack of right operand: {}", this);
            throw new RuntimeException("lack of right operand");
        }
        if (operator == ComparisonOperator.BETWEEN && rightOperands.size() != 2) {
            log.error("right operand length is not 2 when operator is BETWEEN");
            throw new RuntimeException("right operand length is not 2 when operator is BETWEEN");
        }
    }

    public String toQuery() {
        switch (operator) {
            case EQUAL:
            case NOT_EQUAL:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
            case LIKE:
                return String.format(simple, leftOperand, operator.getSymbol(), rightOperands.get(0));
            case BETWEEN:
                return String.format(between, leftOperand, rightOperands.get(0), rightOperands.get(1));
            case IN: {
                StringBuilder sb = new StringBuilder();
                for (String r : rightOperands) {
                    sb.append("'").append(r).append("'").append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                return String.format(in, leftOperand, sb);
            }
            default:
                throw new RuntimeException("operator can not be identify: " + operator.getSymbol());
        }
    }

    @Override
    public void fieldToColumn(DataModel model) {
        DataModel.Column column = model.getFieldMap().get(leftOperand);
        if (column == null) {
            String errMsg = String.format("field %s not exist", leftOperand);
            throw new RuntimeException(errMsg);
        } else {
            leftOperand = column.getColumn();
        }
    }

}
