package com.example.clickhousequery.serializer;

import com.example.clickhousequery.function.TimestampToSecondFunc;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.yandex.clickhouse.util.apache.StringUtils;

import java.io.IOException;
import java.util.function.Function;

@Component
public class TransferDeserializer extends JsonDeserializer<Function<Object, Object>> implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public Function<Object, Object> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        switch (value) {
            case  "TimestampToSecond":
                return new TimestampToSecondFunc();
//                return context.getBean(TimestampToSecondFunc.class);
            default:
                throw new IllegalArgumentException("invalid transfer function: " + value);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
