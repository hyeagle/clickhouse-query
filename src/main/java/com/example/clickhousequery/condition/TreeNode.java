package com.example.clickhousequery.condition;

import com.example.clickhousequery.entity.DataModel;

public interface TreeNode {

    void check();

    String toQuery();

    void fieldToColumn(DataModel model);
}
