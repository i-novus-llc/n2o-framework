package net.n2oapp.framework.config.io;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

/**
 * Хранение параметров метаданной в рамках одного потока
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetadataParamHolder {

    private static final ThreadLocal<Map<String, String[]>> threadLocalScope = new ThreadLocal<>();

    public static Map<String, String[]> getParams() {
        return threadLocalScope.get() == null ? Collections.emptyMap() : threadLocalScope.get();
    }

    public static void setParams(Map<String, String[]> params) {
        if (params == null) removeParams();
        threadLocalScope.set(params);
    }

    public static void removeParams() {
        threadLocalScope.remove();
    }
}