package com.example.clickhousequery.entity;

import com.example.clickhousequery.query.select.KeyArraySelect;
import com.example.clickhousequery.query.select.Select;
import com.example.clickhousequery.query.select.SimpleSelect;
import com.example.clickhousequery.serializer.TransferDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class DataModel {

    private String table;
    @JsonProperty("field")
    private List<Column> columns;
    private Map<String, Column> fieldMap;
    private Map<String, Column> columnMap;

    @Setter
    @Getter
    public static class Column {
        @JsonProperty("name")
        private String field;
        private String column;
        @JsonProperty("nested_col_key")
        private String nestColKey;
        @JsonProperty("nested_col_value")
        private String nestColValue;
        @JsonDeserialize(using = TransferDeserializer.class)
        private Function<Object, Object> transfer;
        private String type;
        @JsonProperty("is_array")
        private boolean array;
    }

    public static DataModel fromYaml(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        DataModel dataModel = mapper.readValue(new ClassPathResource(path).getFile(), DataModel.class);
        dataModel.fieldMap = dataModel.columns.stream().collect(Collectors.toMap(column -> column.field, Function.identity(), (e1, e2) -> e1));
        dataModel.columnMap = dataModel.columns.stream().collect(Collectors.toMap(column -> column.column, Function.identity(), (e1, e2) -> e1));
        return dataModel;
    }

    public List<Select> transferToSelect(String[] fields) {
        if (fields.length == 1 && "*".equals(fields[0])) {
            return columns.stream()
                    .map(e -> e.nestColKey == null ? new SimpleSelect(e.column, e.field) : new KeyArraySelect(e.column, e.field, e.nestColKey, e.nestColValue))
                    .collect(Collectors.toList());
        }

        return Arrays.stream(fields)
                .filter(e -> this.fieldMap.containsKey(e))
                .map(s -> {
                    DataModel.Column column = this.fieldMap.get(s);
                    if (column.nestColKey != null) {
                        return new KeyArraySelect(column.getColumn(), column.getField(), column.nestColKey, column.nestColValue);
                    } else {
                        return new SimpleSelect(column.getColumn(), column.getField());
                    }
                })
                .collect(Collectors.toList());
    }

    public String fieldToColumn(String field) {
        return fieldMap.get(field).column;
    }

    public String columnToField(String column) {
        return columnMap.get(column).field;
    }
}
