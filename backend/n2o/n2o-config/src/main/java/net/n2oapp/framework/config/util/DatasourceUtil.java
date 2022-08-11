package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.config.metadata.compile.datasource.ApplicationDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилита для компиляции источников данных
 */
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
     * Получение списка идентификаторов клиентских источников данных
     *
     * @param datasourceIds массив идентификаторов источников данных
     * @param p Процессор сборки метаданных
     * @return Список идентификаторов клиенстких источников данных
     */
    public static List<String> getClientDatasourceIds(List<String> datasourceIds, CompileProcessor p) {
        return datasourceIds.stream().map(ds -> getClientDatasourceId(ds, p)).collect(Collectors.toList());
    }

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
     * Получение идентификатора клиентского источника данных.
     *
     * @param datasourceId Идентификатор источника данных
     * @param pageId       Идентификатор страницы
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientDatasourceId(String datasourceId, String pageId) {
        if (datasourceId == null || pageId == null)
            return datasourceId;
        String separator = "_".equals(pageId) ? "" : "_";
        return pageId.concat(separator).concat(datasourceId);
    }
}
