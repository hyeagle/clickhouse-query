package com.example.clickhousequery.condition.compare;

public enum ComparisonOperator {
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    IN("in"),
    BETWEEN("between"),
    LIKE("like");

    private final String symbol;

    ComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
