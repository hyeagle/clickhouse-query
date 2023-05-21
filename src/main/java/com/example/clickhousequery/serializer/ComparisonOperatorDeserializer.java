package com.example.clickhousequery.serializer;

import com.example.clickhousequery.condition.compare.ComparisonOperator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.yandex.clickhouse.util.apache.StringUtils;

import java.io.IOException;

public class ComparisonOperatorDeserializer extends JsonDeserializer<ComparisonOperator> {

    @Override
    public ComparisonOperator deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (ComparisonOperator op : ComparisonOperator.values()) {
            if (op.getSymbol().equals(value)) {
                return op;
            }
        }
        throw new IllegalArgumentException("invalid comparison operator value: " + value);
    }
}