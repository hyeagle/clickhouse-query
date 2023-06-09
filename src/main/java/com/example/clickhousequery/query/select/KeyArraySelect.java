package com.example.clickhousequery.query.select;

public class KeyArraySelect implements Select {

    private static final String query = "?2[indexOf(?1, '?3')] AS ?4";

    public final String name;
    public final String alias;
    public final String keyArray;
    public final String valueArray;

    public KeyArraySelect(String name, String alias, String keyArray, String valueArray) {
        this.name = name;
        this.alias = alias;
        this.keyArray = keyArray;
        this.valueArray = valueArray;
    }

    public String query() {
        return query.replace("?1", keyArray)
                .replace("?2", valueArray)
                .replace("?4", alias)
                .replace("?3", name);
    }

}
