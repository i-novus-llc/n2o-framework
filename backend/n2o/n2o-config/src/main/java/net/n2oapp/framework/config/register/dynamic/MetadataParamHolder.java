package net.n2oapp.framework.config.register.dynamic;

import java.util.Map;

/**
 * Хранение параметров метаданной в рамках одного потока
 */
public class MetadataParamHolder {

    private static final ThreadLocal<Map<String, String>> threadLocalScope = new ThreadLocal<>();

    public final static Map<String, String> getParams() {
        return threadLocalScope.get();
    }

    public final static void setParams(Map<String, String> params) {
        threadLocalScope.set(params);
    }
}
