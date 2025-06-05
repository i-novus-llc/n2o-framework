package net.n2oapp.framework.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.util.RefUtil;

import java.util.*;
import java.util.stream.Stream;

/**
 * Утилиты для работы с динамическими метаданными
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DynamicUtil {

    /**
     * Содержит ли идентифкатор метаданной ссылки на данные
     * @param id идентификатор
     * Примеры:
     *  isRef("{name}"); //true
     *  isRef("my_{name}"); //true
     *  isRef("my_{name}_{surname}"); //true
     *  isRef("test"); //false
     */
    public static boolean hasRefs(String id) {
        return RefUtil.hasRefs(id);
    }

    /**
     * Является ли метаданная динамической?
     * @param id Идентификатор
     * Примеры:
     *  isDynamic("code?context"); //true
     *  isDynamic("test"); //false
     */
    public static boolean isDynamic(String id) {
        return id.contains("?");
    }

    public static void checkDynamicIds(List<String> ids, String code) {
        if (ids != null)
            ids.forEach(id -> {
                if (!isDynamic(id))
                    throw new IllegalArgumentException (String.format("Провайдер динамических метаданных dynamic-metadata-provider [%s] использует нединамический идентификатор [%s]", code, id));
                else if (!id.startsWith(code))
                    throw new IllegalArgumentException (String.format("Провайдер динамических метаданных dynamic-metadata-provider [%s] использует идентификатор [%s] с некорректным префиксом", code, id));
            });
    }

    public static String resolveTokens(String metadataId, String... tokens) {
        String id = metadataId;
        if (hasRefs(metadataId) && tokens != null) {
            Set<String> refs = RefUtil.getRefs(metadataId);
            Map<String, String> placeholders = new HashMap<>();
            int i = 0;
            for (String ref : refs) {
                if (i >= tokens.length)
                    throw new IllegalStateException("Число ссылок в metadataId больше числа предоставленных токенов: metadataId = [" + metadataId + "], tokens = " + Arrays.asList(tokens));
                placeholders.put(ref, tokens[i]);
                i++;
            }
            id = RefUtil.resolve(metadataId, placeholders);
        }
        return id;
    }

    public static String reduceTokens(String metadataId, String... tokens) {
        if (tokens != null && tokens.length > 0) {
            return metadataId + "$" + Stream.of(tokens).reduce((a, b) -> a + "," + b).get();
        }
        return metadataId;
    }
}
