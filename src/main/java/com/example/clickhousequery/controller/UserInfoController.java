package com.example.clickhousequery.controller;

import com.example.clickhousequery.condition.TreeNode;
import com.example.clickhousequery.condition.compare.CompareTreeNode;
import com.example.clickhousequery.condition.compare.ComparisonOperator;
import com.example.clickhousequery.condition.logic.LogicTreeNode;
import com.example.clickhousequery.condition.logic.LogicalOperator;
import com.example.clickhousequery.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserInfoController {

    private final UserInfoService service;

    @ResponseBody
    @GetMapping("/page")
    public Object queryPrimary() {
        TreeNode treeNode = LogicTreeNode
                .buildWithOp(LogicalOperator.OR)
                .addChild(new CompareTreeNode(ComparisonOperator.EQUAL, "username", Lists.list("ross1")))
                .addChild(new CompareTreeNode(ComparisonOperator.IN, "phone", Lists.list("20002")));
        return service.queryPrimary(treeNode, "username", null, 1, 2);
    }
}
