package com.example.clickhousequery.query.where;

import com.example.clickhousequery.query.Operator;
import ru.yandex.clickhouse.ClickHouseUtil;

public class DatetimeWhere implements Where {

    private final String alias;
    private final Operator op;
    private final String value;

    public DatetimeWhere(String alias, Operator op, String value) {
        this.alias = alias;
        this.op = op;
        this.value = value;
    }

    @Override
    public String condition() {
        String s = ClickHouseUtil.escape(value);
        switch (op) {
            case Equal:
                return alias + " =  toDateTime(" + s + ", 'Universal')";
            case NotEqual:
                return alias + " != toDateTime(" + s + ", 'Universal')";
            case Less:
                return alias + " < toDateTime(" + s + ", 'Universal')";
            case LessEqual:
                return alias + " <= toDateTime(" + s + ", 'Universal')";
            case More:
                return alias + " > toDateTime(" + s + ", 'Universal')";
            case MoreEqual:
                return alias + " >= toDateTime(" + s + ", 'Universal')";
            default:
                return null;
        }
    }
}
