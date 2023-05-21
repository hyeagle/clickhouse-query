package com.example.clickhousequery.service.impl;

import com.example.clickhousequery.condition.TreeNode;
import com.example.clickhousequery.entity.DataModel;
import com.example.clickhousequery.query.select.Select;
import com.example.clickhousequery.repository.UserInfoDao;
import com.example.clickhousequery.service.UserInfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoDao dao;

    private final DataModel dataModel;

    private static final String[] primaryField = new String[]{
            "name", "age", "registerTime", "familyMember", "address", "deviceId"
    };

    public UserInfoServiceImpl(UserInfoDao dao, @Qualifier("userModel") DataModel dataModel) {
        this.dao = dao;
        this.dataModel = dataModel;
    }

    @Override
    public List<Map<String, Object>> queryPrimary(TreeNode treeNode, String sort, String direction, int page, int size) {
        List<Select> selects = dataModel.transferToSelect(primaryField);
        treeNode.fieldToColumn(dataModel);
        sort = dataModel.columnToField(sort);
        if (direction == null || !direction.equals("DESC")) {
            direction = "ASC";
        }
        List<Map<String, Object>> results = dao.query(dataModel.getTable(), selects, treeNode, sort, direction, size, (page - 1) * size);
        for (Map<String, Object> result : results) {
            Set<String> keySet = new HashSet<>(result.keySet());
            for (String key : keySet) {
                Function<Object, Object> transfer = dataModel.getColumnMap().get(key).getTransfer();
                if (transfer != null) {
                    result.put(dataModel.fieldToColumn(key), transfer.apply(result.get(key)));
                } else {
                    result.put(dataModel.fieldToColumn(key), result.get(key));
                }
                result.remove(key);
            }
        }
        return results;
    }
}
