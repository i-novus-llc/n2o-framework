package net.n2oapp.framework.api;

import net.n2oapp.framework.api.util.RefUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Утилиты для работы с динамическими метаданными
 */
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
     * @param id едентификатор
     * Примеры:
     *  isDynamic("code$context"); //true
     *  isDynamic("test"); //false
     */
    public static boolean isDynamic(String id) {
        return id.contains("?");
    }

    public static void checkDynamicIds(List<String> ids, String code) {
        if (ids != null)
            ids.forEach(id -> {
                if (!isDynamic(id))
                    throw new RuntimeException(String.format("dynamic-metadata-provider [%s] provides not dynamic id [%s]", code, id));
                else if (!id.startsWith(code))
                    throw new RuntimeException(String.format("dynamic-metadata-provider [%s]  provides wrong id [%s]", code, id));
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
                    throw new IllegalStateException("References more than tokens: metadataId = [" + metadataId + "], tokens = " + Arrays.asList(tokens));
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
