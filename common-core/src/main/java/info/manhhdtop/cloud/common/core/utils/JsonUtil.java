package info.manhhdtop.cloud.common.core.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Slf4j
@SuppressWarnings("unused")
public class JsonUtil {
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new JavaTimeModule())
                .build();
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.addMixIn(Throwable.class, ThrowableMixin.class);
    }

    private JsonUtil() {
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        String json = null;
        try {
            json = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("(toJson) JsonProcessingException: {}", ex.getMessage(), ex);
        }
        return json;
    }

    public static <T> T toObject(String json, Class<T> type) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        T t = null;
        try {
            t = OBJECT_MAPPER.readValue(json, type);
        } catch (Exception ex) {
            log.error("(toObject) JsonProcessingException: {}", ex.getMessage(), ex);
        }
        return t;
    }

    public static <T> T toObject(String json, Class<T> type, Class<?>... parameterTypes) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        T t = null;
        try {
            JavaType javaType = constructParametricType(type, parameterTypes);
            t = OBJECT_MAPPER.readValue(json, javaType);
        } catch (Exception ex) {
            log.error("(toObject) Exception: {}", ex.getMessage(), ex);
        }
        return t;
    }

    public static <T> T toObject(Map<String, String> map, Class<T> type, Class<?>... parameterTypes) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        JavaType javaType = constructParametricType(type, parameterTypes);
        return OBJECT_MAPPER.convertValue(map, javaType);
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> keyType, Class<V> valueType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        Map<K, V> map = null;
        try {
            JavaType javaType = constructParametricType(Map.class, keyType, valueType);
            map = OBJECT_MAPPER.readValue(json, javaType);
        } catch (Exception ex) {
            log.error("(toMap) Exception: {}", ex.getMessage(), ex);
        }
        return map;
    }

    private static JavaType constructParametricType(Class<?> rawType, Class<?>... parameterTypes) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(rawType, parameterTypes);
    }

    public static <T extends Collection<E>, E> T toCollection(String json, Class<T> collectionType, Class<E> elementType) {
        T t = null;
        JavaType type = constructCollectionType(collectionType, elementType);
        try {
            t = OBJECT_MAPPER.readValue(json, type);
        } catch (Exception ex) {
            log.error("(toCollection) Exception: {}", ex.getMessage(), ex);
        }
        return t;
    }

    @SuppressWarnings("rawtypes")
    private static JavaType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementType) {
        return OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementType);
    }

    public static <SRC, DES> DES convert(SRC src, Class<DES> desClass, Class<?>... parameterTypes) {
        if (Objects.isNull(src)) {
            return null;
        }
        DES des = null;
        try {
            JavaType javaType = constructParametricType(desClass, parameterTypes);
            des = OBJECT_MAPPER.convertValue(src, javaType);
        } catch (Exception ex) {
            log.error("(map) Exception: {}", ex.getMessage(), ex);
        }
        return des;
    }

    public static <SRC, DES> void update(SRC src, DES des) {
        if (Objects.isNull(src) || Objects.isNull(des)) {
            return;
        }
        try {
            OBJECT_MAPPER.updateValue(des, src);
        } catch (Exception ex) {
            log.error("(map) Exception: {}", ex.getMessage(), ex);
        }
    }

    @JsonIgnoreProperties({"stackTrace", "suppressed", "localizedMessage"})
    public abstract static class ThrowableMixin {
    }
}
