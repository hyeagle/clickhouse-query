package com.example.clickhousequery.repository;

import com.example.clickhousequery.condition.TreeNode;
import com.example.clickhousequery.query.select.Select;

import java.util.List;
import java.util.Map;

public interface UserInfoDao {

    List<Map<String, Object>> query(String table, List<Select> selects, TreeNode where, String sort, String direction, int limit, int offset);

}
