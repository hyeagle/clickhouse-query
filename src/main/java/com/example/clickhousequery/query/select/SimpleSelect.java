package com.example.clickhousequery.query.select;

import org.apache.logging.log4j.util.Strings;

public class SimpleSelect implements Select {

    private final String name;
    private final String alias;

    public SimpleSelect(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String query() {
        if (Strings.isEmpty(alias)) {
            return name;
        } else {
            return name + " AS " + alias;
        }
    }
}
