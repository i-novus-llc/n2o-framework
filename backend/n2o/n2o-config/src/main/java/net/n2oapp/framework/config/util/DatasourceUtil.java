package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.ApplicationDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.List;
import java.util.Optional;
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
     * @param p             Процессор сборки метаданных
     * @return Список идентификаторов клиенстких источников данных
     */
    public static List<String> getClientDatasourceIds(List<String> datasourceIds, CompileProcessor p) {
        return datasourceIds.stream().map(ds -> getClientDatasourceId(ds, p)).collect(Collectors.toList());
    }

    /**
     * Получение идентификатора клиентского источника данных в случае, если он прокинут из родительской страницы
     *
     * @param datasourceId Идентификатор источника данных
     * @param context      Контекст сборки метаданных
     * @param p            Процессор сборки метаданных
     * @return Идентификатор клиентского источника данных
     */
    @Deprecated
    public static String getClientDatasourceId(String datasourceId, CompileContext context, CompileProcessor p) {
        DataSourcesScope datasourcesScope = p.getScope(DataSourcesScope.class);
        if (context instanceof PageContext && datasourcesScope != null && !datasourcesScope.containsKey(datasourceId)) {
            // проверка, что источник данных прокинут с родительской страницы
            PageContext pageContext = (PageContext) context;
            Optional<N2oAbstractDatasource> parentDatasource = Optional.ofNullable(pageContext.getParentDatasources()).map(m -> m.get(datasourceId));
            if (parentDatasource.isPresent()) {
                if (parentDatasource.get() instanceof N2oApplicationDatasource)
                    return datasourceId;
                return getClientDatasourceId(datasourceId, ((PageContext) context).getParentClientPageId());
            }
        }
        return getClientDatasourceId(datasourceId, p);
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
     * Получение идентификатора клиентского источника данных.
     *
     * @param datasourceId Идентификатор источника данных
     * @param pageId       Идентификатор страницы
     * @param p            Процессор сборки метаданных
     * @return Идентификатор клиентского источника данных
     */
    public static String getClientDatasourceId(String datasourceId, String pageId, CompileProcessor p) {
        if (datasourceId == null || pageId == null)
            return datasourceId;

        ApplicationDatasourceIdsScope appDatasourceIds = p.getScope(ApplicationDatasourceIdsScope.class);
        if (appDatasourceIds != null && appDatasourceIds.contains(datasourceId))
            return datasourceId;

        String separator = "_".equals(pageId) ? "" : "_";
        return pageId.concat(separator).concat(datasourceId);
    }
}
