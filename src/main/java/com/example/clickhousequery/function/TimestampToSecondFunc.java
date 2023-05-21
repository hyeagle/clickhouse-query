package com.example.clickhousequery.function;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.function.Function;

@Component
public class TimestampToSecondFunc implements Function<Object, Object> {
    @Override
    public Object apply(Object obj) {
        if (obj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) obj;
            return timestamp.getTime() / 1000;
        }
        String err = String.format("input type should be java.sql.Timestamp rather than: %s", obj.getClass().getName());
        throw new RuntimeException(err);
    }
}
