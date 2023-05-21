package com.example.clickhousequery.service;

import com.example.clickhousequery.condition.TreeNode;

import java.util.List;
import java.util.Map;

public interface UserInfoService {

    List<Map<String, Object>> queryPrimary(TreeNode treeNode, String sort, String direction, int page, int size);


}
