package com.example.clickhousequery.condition;

import com.example.clickhousequery.condition.compare.CompareTreeNode;
import com.example.clickhousequery.condition.compare.ComparisonOperator;
import com.example.clickhousequery.condition.logic.LogicTreeNode;
import com.example.clickhousequery.condition.logic.LogicalOperator;
import com.example.clickhousequery.util.FieldUtil;
import org.assertj.core.util.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SnubaQuery {

    public static TreeNode fromSnubaQuery(String query) {
        return fromSnubaQuery(convertToPrefix(query));
    }

    private static TreeNode fromSnubaQuery(Stack<String> elements) {
        if (!elements.empty()) {
            String element = elements.pop();
            if (isOperator(element)) {
                TreeNode node1 = fromSnubaQuery(elements);
                TreeNode node2 = fromSnubaQuery(elements);
                return new LogicTreeNode(LogicalOperator.fromSymbol(element), Lists.newArrayList(node1, node2));
            } else {
                return fromSingleQuery(element);
            }
        }
        return null;
    }

    private static Stack<String> convertToPrefix(String infixExpression) {
        Stack<String> prefix = new Stack<>();
        Stack<String> stack = new Stack<>();

        List<String> tokens =
                Arrays.stream(infixExpression.replace("(", " ( ")
                        .replace(")", " ) ")
                        .split(" "))
                        .filter(e -> !e.isEmpty())
                        .collect(Collectors.toList());

        for (int i = tokens.size() - 1; i >= 0; i--) {
            String token = tokens.get(i);

            if (token.equals(")")) {
                stack.push(token);
            } else if (token.equals("(")) {
                while (!stack.isEmpty() && !stack.peek().equals(")")) {
                    prefix.add(stack.pop());
                }
                stack.pop(); // Pop ')'
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && precedence(token) <= precedence(stack.peek())) {
                    prefix.add(stack.pop());
                }
                stack.push(token);
            } else {
                prefix.add(token);
            }
        }

        while (!stack.isEmpty()) {
            prefix.add(stack.pop());
        }

        return prefix;
    }

    private static boolean isOperator(String token) {
        for (LogicalOperator op : LogicalOperator.values()) {
            if (op.getSymbol().equals(token))
                return true;
        }
        return false;
    }

    private static int precedence(String operator) {
        String op = operator.toLowerCase();
        switch (op) {
            case "and":
                return 2;
            case "or":
                return 1;
            default:
                return 0;
        }
    }

    private static TreeNode fromSingleQuery(String expression) {
        if (expression.contains(":>=")) {
            String[] split = expression.split(":>=");
            return new CompareTreeNode(ComparisonOperator.GREATER_THAN_OR_EQUAL, FieldUtil.underscoreToCamelCase(split[0]), Lists.list(split[1]));
        } else if (expression.contains(":>")) {
            String[] split = expression.split(":>");
            return new CompareTreeNode(ComparisonOperator.GREATER_THAN, FieldUtil.underscoreToCamelCase(split[0]), Lists.list(split[1]));
        } else if (expression.contains(":<=")) {
            String[] split = expression.split(":<=");
            return new CompareTreeNode(ComparisonOperator.LESS_THAN_OR_EQUAL, FieldUtil.underscoreToCamelCase(split[0]), Lists.list(split[1]));
        } else if (expression.contains(":<")) {
            String[] split = expression.split(":<");
            return new CompareTreeNode(ComparisonOperator.LESS_THAN, FieldUtil.underscoreToCamelCase(split[0]), Lists.list(split[1]));
        } else if (expression.contains(":")) {
            String[] split = expression.split(":");
            if (split[1].startsWith("[") && split[1].endsWith("]")) {
                List<String> collect = Arrays.stream(split[1].substring(1, split[1].length() - 1).split(","))
                        .collect(Collectors.toList());
                return new CompareTreeNode(ComparisonOperator.IN, FieldUtil.underscoreToCamelCase(split[0]), collect);
            } else if (split[1].contains("*")) {
                return new CompareTreeNode(ComparisonOperator.LIKE, FieldUtil.underscoreToCamelCase(split[0]), Lists.list(split[1].replace("*", "%")));
            } else if (split[0].startsWith("!")) {
                return new CompareTreeNode(ComparisonOperator.NOT_EQUAL, FieldUtil.underscoreToCamelCase(split[0].substring(1)), Lists.list(split[1]));
            } else {
                return new CompareTreeNode(ComparisonOperator.IN, FieldUtil.underscoreToCamelCase(split[0]), Lists.list(split[1]));
            }
        }
        throw new RuntimeException("snuba single query transfer to TreeNode fail");
    }


    public static void main(String[] args) {
        String infixExpression = "(event_severity:0 OR event_uuid:455220421315431781) AND threat_attck_tecs:T1088 AND client_id:ad";
        Stack<String> prefix = convertToPrefix(infixExpression);
        TreeNode treeNode = fromSnubaQuery(prefix);
        while (!prefix.empty()) {
            System.out.println(prefix.pop());
        }
    }
}
