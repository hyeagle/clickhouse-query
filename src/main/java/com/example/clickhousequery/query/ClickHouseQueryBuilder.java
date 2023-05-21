package com.example.clickhousequery.query;

import com.example.clickhousequery.condition.TreeNode;
import com.example.clickhousequery.query.select.Select;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ClickHouseQueryBuilder {

    private List<Select> selectClause = new ArrayList<>();
    private String fromClause = "";
    private TreeNode whereClause = null;
    private List<String> orderByClauses = new ArrayList<>();
    private String direction = "";
    private int limit;
    private int offset;


    public ClickHouseQueryBuilder select(Select selectClause) {
        this.selectClause.add(selectClause);
        return this;
    }

    public ClickHouseQueryBuilder select(List<Select> selectClauses) {
        selectClause = selectClauses;
        return this;
    }

    public ClickHouseQueryBuilder from(String fromClause) {
        this.fromClause = fromClause;
        return this;
    }

    public ClickHouseQueryBuilder where(TreeNode whereClause) {
        this.whereClause = whereClause;
        return this;
    }

    public ClickHouseQueryBuilder orderBy(String orderByClause) {
        this.orderByClauses.add(orderByClause);
        return this;
    }

    public ClickHouseQueryBuilder orderBy(List<String> orderByClause) {
        this.orderByClauses = orderByClause;
        return this;
    }

    public ClickHouseQueryBuilder direction(String direction) {
        this.direction = direction;
        return this;
    }

    public ClickHouseQueryBuilder limit(int limit) {
        return limit(0, limit);
    }

    public ClickHouseQueryBuilder limit(int offset, int limit) {
        this.limit = limit;
        this.offset = offset;
        return this;
    }

    public String buildSql() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ").append(select());
        sqlBuilder.append(" FROM ").append(fromClause);
        if (whereClause != null) {
            sqlBuilder.append(" WHERE ").append(whereClause.toQuery());
        }
        if (!orderByClauses.isEmpty()) {
            sqlBuilder.append(" ORDER BY ");
            sqlBuilder.append(String.join(",", orderByClauses));
        }
        if (!direction.isEmpty()) {
            sqlBuilder.append(" ").append(direction).append(" ");
        }
        if (limit != 0) {
            sqlBuilder.append(" LIMIT ").append(offset).append(", ").append(limit);
        }
        return sqlBuilder.toString();
    }

    private String select() {
        if (CollectionUtils.isEmpty(selectClause))
            return "*";
        else {
            StringBuilder sb = new StringBuilder();
            for (Select s : selectClause) {
                sb.append(s.query()).append(",");
            }
            return sb.substring(0, sb.length() - 1);
        }
    }

}

