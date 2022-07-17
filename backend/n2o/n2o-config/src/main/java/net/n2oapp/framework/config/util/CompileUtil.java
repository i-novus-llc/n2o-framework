package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.metadata.compile.datasource.ApplicationDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Утилита для генерации различных свойств во время компиляции
 */
public class CompileUtil {

    /**
     * Получение идентификатора клиентского источника данных
     *
     * @param datasourceId Идентификатор источника данных
     * @param p            Процессор сборки метаданных
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientDatasourceId(String datasourceId, CompileProcessor p) {
        ApplicationDatasourceIdsScope appDatasourceIds = p.getScope(ApplicationDatasourceIdsScope.class);
        if (appDatasourceIds != null && appDatasourceIds.contains(datasourceId))
            return datasourceId;

        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope == null)
            return datasourceId;
        return getClientDatasourceId(datasourceId, pageScope.getPageId());
    }

    /**
     * Получение идентификатора клиентского источника данных
     *
     * @param widgetId  Идентификатор источника данных
     * @param p            Процессор сборки метаданных
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientWidgetId(String widgetId, CompileProcessor p) {
        return getClientDatasourceId(widgetId, p);
    }

    /**
     * Получение идентификатора клиентского источника данных
     *
     * @param datasourceId Идентификатор источника данных
     * @param pageId       Идентификатор страницы
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientDatasourceId(String datasourceId, String pageId) {
        if (datasourceId == null)
            return null;
        String separator = "_".equals(pageId) ? "" : "_";
        return pageId.concat(separator).concat(datasourceId);
    }

    /**
     * Преобразовывает плоскую мапу в объемную по знаку "-" в ключе
     * a-b : 123 -> a : {b : 123}
     *
     * @param attributes  плоская мапа атрибутов
     * @param transformer фукнция преобразования значения
     * @return объемная мапа атрибутов
     */
    public static Map<String, Object> resolveNestedAttributes(Map<String, Object> attributes, Function<Object, Object> transformer) {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String[] keyChain = entry.getKey().split("-");
            Map<String, Object> nested = result;
            for (int i = 0; i < keyChain.length - 1; i++) {
                if (!nested.containsKey(keyChain[i])) {
                    nested.put(keyChain[i], new HashMap<>());
                }
                if (!HashMap.class.equals(nested.get(keyChain[i]).getClass())) {
                    throw new IllegalArgumentException("The result already contains an element with key " + keyChain[i]);
                }
                nested = (Map<String, Object>) nested.get(keyChain[i]);
            }
            if (nested.containsKey(keyChain[keyChain.length - 1])) {
                throw new IllegalArgumentException("The result already contains an element with key " + keyChain[keyChain.length - 1]);
            }
            nested.put(keyChain[keyChain.length - 1], transformer.apply(entry.getValue()));
        }
        return result;
    }
}
