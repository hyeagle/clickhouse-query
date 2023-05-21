package com.example.clickhousequery.query.where;

import com.example.clickhousequery.query.Operator;
import ru.yandex.clickhouse.ClickHouseUtil;

public class SimpleWhere implements Where {

    private final String alias;
    private final Operator op;
    private final Object value;

    public SimpleWhere(String alias, Operator op, Object value) {
        this.alias = alias;
        this.op = op;
        this.value = value;
    }

    public String condition() {
        String s = ClickHouseUtil.escape(String.valueOf(value));
        switch (op) {
            case Equal:
                return alias + " = '" + s + "'";
            case NotEqual:
                return alias + " != '" + s + "'";
            case Less:
                return alias + " < '" + s + "'";
            case LessEqual:
                return alias + " <= '" + s + "'";
            case More:
                return alias + " > '" + s + "'";
            case MoreEqual:
                return alias + " >= '" + s + "'";
            default:
                return null;
        }

    }
}
