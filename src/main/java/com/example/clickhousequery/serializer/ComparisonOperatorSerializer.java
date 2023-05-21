package com.example.clickhousequery.serializer;

import com.example.clickhousequery.condition.compare.ComparisonOperator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ComparisonOperatorSerializer extends JsonSerializer<ComparisonOperator> {

    @Override
    public void serialize(ComparisonOperator op, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(op.getSymbol());
    }
}