package com.example.clickhousequery.condition.logic;

public enum LogicalOperator {
    AND("AND"),
    OR("OR");

    private final String symbol;

    LogicalOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static LogicalOperator fromSymbol(String s) {
        for (LogicalOperator op : LogicalOperator.values()) {
            if (op.getSymbol().equalsIgnoreCase(s))
                return op;
        }
        throw new RuntimeException("can not identify symbol as LogicalOperator");
    }
}

