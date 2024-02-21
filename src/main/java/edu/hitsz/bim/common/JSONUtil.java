package edu.hitsz.bim.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换为JSON字符串。
     * @param object 要转换的对象。
     * @return JSON字符串。
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 将JSON字符串转换为指定类型的对象。
     * @param jsonStr JSON字符串。
     * @param valueType 对象的目标类型。
     * @return 目标类型的对象。
     */
    public static <T> T fromJsonString(String jsonStr, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
