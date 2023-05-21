package com.example.clickhousequery.serializer;

import com.example.clickhousequery.condition.logic.LogicalOperator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.yandex.clickhouse.util.apache.StringUtils;

import java.io.IOException;

public class LogicalOperatorDeserializer extends JsonDeserializer<LogicalOperator> {

    @Override
    public LogicalOperator deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (LogicalOperator op : LogicalOperator.values()) {
            if (op.getSymbol().equals(value)) {
                return op;
            }
        }
        throw new IllegalArgumentException("invalid logic operator value: " + value);
    }
}