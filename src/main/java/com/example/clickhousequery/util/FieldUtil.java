package com.example.clickhousequery.util;

public class FieldUtil {

    /**
     * 驼峰式命名转下划线式命名
     *
     * @param camelCase 驼峰式命名的字符串
     * @return 下划线式命名的字符串
     */
    public static String camelCaseToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.append('_');
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * 下划线式命名转驼峰式命名
     *
     * @param underscore 下划线式命名的字符串
     * @return 驼峰式命名的字符串
     */
    public static String underscoreToCamelCase(String underscore) {
        if (underscore == null || underscore.isEmpty()) {
            return underscore;
        }
        StringBuilder builder = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < underscore.length(); i++) {
            char c = underscore.charAt(i);
            if (c == '_') {
                upperCase = true;
            } else {
                if (upperCase) {
                    builder.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    builder.append(c);
                }
            }
        }
        return builder.toString();
    }
}
