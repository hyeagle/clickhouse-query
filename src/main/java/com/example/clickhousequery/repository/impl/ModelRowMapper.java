package com.example.clickhousequery.repository.impl;

import com.example.clickhousequery.entity.DataModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModelRowMapper implements RowMapper<Map<String, Object>> {

    private final DataModel model;

    public ModelRowMapper(DataModel model) {
        this.model = model;
    }

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Map<String, Object> m = new HashMap<>();
        int columnCount = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            if (model.getFieldMap().containsKey(columnName)) {
                Function<Object, Object> func = model.getFieldMap().get(columnName).getTransfer();
                if (func == null) {
                    m.put(columnName, rs.getObject(i));
                } else {
                    m.put(columnName, func.apply(rs.getObject(i)));
                }
            }
        }
        return m;
    }
}
