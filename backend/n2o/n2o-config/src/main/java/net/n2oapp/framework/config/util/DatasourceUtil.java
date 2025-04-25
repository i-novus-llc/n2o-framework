package net.n2oapp.framework.config.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.metadata.compile.datasource.ClientDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилита для компиляции источников данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatasourceUtil {

    /**
     * Получение идентификатора клиентского виджета
     *
     * @param widgetId Идентификатор виджета
     * @param pageId   Идентификатор страницы
     * @return Идентификатор клиентского виджета
     */
    public static String getClientWidgetId(String widgetId, String pageId) {
        return getClientDatasourceId(widgetId, pageId);
    }

    /**
     * Получение идентификатора клиентского источника данных.
     *
     * @param datasourceId Идентификатор источника данных
     * @param pageId       Идентификатор страницы
     * @return Идентификатор клиентского источника данных
     */
    @Deprecated
    public static String getClientDatasourceId(String datasourceId, String pageId) {
        if (datasourceId == null || pageId == null)
            return datasourceId;
        String separator = "_".equals(pageId) ? "" : "_";
        return pageId.concat(separator).concat(datasourceId);
    }

    /**
     * Получение списка идентификаторов клиентских источников данных
     *
     * @param datasourceIds массив идентификаторов источников данных
     * @param p             Процессор сборки метаданных
     * @return Список идентификаторов клиенстких источников данных
     */
    public static List<String> getClientDatasourceIds(List<String> datasourceIds, CompileProcessor p) {
        return datasourceIds.stream().map(ds -> getClientDatasourceId(ds, p)).toList();
    }

    /**
     * Получение идентификатора клиентского источника данных
     *
     * @param datasourceId Идентификатор источника данных
     * @param p            Процессор сборки метаданных
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientDatasourceId(String datasourceId, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope == null)
            return datasourceId;
        return getClientDatasourceId(datasourceId, pageScope.getPageId(), p);
    }

    /**
     * Получение идентификатора клиентского источника данных
     *
     * @param datasourceId Идентификатор источника данных
     * @param pageId       Идентификатор страницы
     * @param clientDatasourceIdsScope Список источников данных приложения
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientDatasourceId(String datasourceId, String pageId,
                                               ClientDatasourceIdsScope clientDatasourceIdsScope) {
        if (datasourceId == null || pageId == null)
            return datasourceId;

        if (clientDatasourceIdsScope != null && clientDatasourceIdsScope.containsKey(datasourceId))
            return clientDatasourceIdsScope.get(datasourceId);

        String separator = "_".equals(pageId) ? "" : "_";
        return pageId.concat(separator).concat(datasourceId);
    }


    /**
     * Получение идентификатора клиентского источника данных.
     *
     * @param datasourceId Идентификатор источника данных
     * @param pageId       Идентификатор страницы
     * @param p            Процессор сборки метаданных
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientDatasourceId(String datasourceId, String pageId, CompileProcessor p) {
        return getClientDatasourceId(datasourceId, pageId,
                p.getScope(ClientDatasourceIdsScope.class));
    }
}
