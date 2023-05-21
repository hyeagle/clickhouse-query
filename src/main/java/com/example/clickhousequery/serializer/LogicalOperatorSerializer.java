package com.example.clickhousequery.serializer;

import com.example.clickhousequery.condition.logic.LogicalOperator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LogicalOperatorSerializer extends JsonSerializer<LogicalOperator> {

    @Override
    public void serialize(LogicalOperator op, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(op.getSymbol());
    }
}