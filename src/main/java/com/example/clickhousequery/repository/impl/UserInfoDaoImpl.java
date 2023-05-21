package com.example.clickhousequery.repository.impl;

import com.example.clickhousequery.condition.TreeNode;
import com.example.clickhousequery.query.ClickHouseQueryBuilder;
import com.example.clickhousequery.query.select.Select;
import com.example.clickhousequery.repository.UserInfoDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserInfoDaoImpl implements UserInfoDao {

    private final JdbcTemplate jdbcTemplate;

    private final ModelRowMapper rowMapper;

    public UserInfoDaoImpl(JdbcTemplate jdbcTemplate,
                           @Qualifier("userMapper") ModelRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Map<String, Object>> query(String table, List<Select> selects, TreeNode where, String sort, String direction, int limit, int offset) {
        String sql = new ClickHouseQueryBuilder()
                .from(table)
                .select(selects)
                .where(where)
                .orderBy(sort)
                .direction(direction)
                .limit(offset, limit)
                .buildSql();
        return jdbcTemplate.query(sql, rowMapper);
    }
}
