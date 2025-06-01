package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

public class PageContextCompileUtil {

    private PageContextCompileUtil() {
    }

    /**
     * Проверка соответствия маршрута и параметров путей
     *
     * @param route      Маршрут
     * @param pathParams Массив параметров путей
     * @param routeScope Информация о текущем маршруте метаданной
     */
    public static void validatePathAndRoute(String route, N2oParam[] pathParams, ParentRouteScope routeScope) {
        List<String> routeParams = route == null ? null : RouteUtil.getParams(route);
        if (CollectionUtils.isEmpty(routeParams) && ArrayUtils.isEmpty(pathParams))
            return;

        if (routeParams == null)
            throw new N2oException(String.format("Параметр пути '%s' не используется в маршруте", pathParams[0].getName()));
        if (pathParams == null)
            throw new N2oException(String.format("Параметр пути '%s' для маршрута '%s' не установлен", routeParams.get(0), route));

        for (N2oParam pathParam : pathParams) {
            if (!routeParams.contains(pathParam.getName()))
                throw new N2oException(String.format("Маршрут '%s' не содержит параметр пути '%s'", route, pathParam.getName()));
            if (routeScope != null && routeScope.getUrl() != null && RouteUtil.getParams(routeScope.getUrl()).contains(pathParam.getName()))
                throw new N2oException(String.format("Параметр пути '%s' дублируется в родительском 'url'", pathParam.getName()));
        }
    }

    /**
     * Добавление параметров
     *
     * @param params        Список входящих параметров запроса
     * @param pathMapping   Map моделей параметров пути
     * @param targetMapping Итоговый Map моделей параметров
     *                      В нее будут добавлены модели построенных параметров
     * @param p             Процессор сборки метаданных
     */
    public static void initMapping(N2oParam[] params, Map<String, ModelLink> pathMapping,
                                   Map<String, ModelLink> targetMapping, CompileProcessor p) {
        if (ArrayUtils.isEmpty(params))
            return;

        targetMapping.putAll(initParams(Arrays.asList(params), pathMapping, p));
    }

    /**
     * Инициализация соответствия идентификаторов источников данных родительской страницы с клиентскими идентификаторами
     *
     * @param context Контекст страницы
     * @param p       Процессор сборки метаданных
     * @return Map соответствий идентификаторов источников данных родительской страницы с клиентскими идентификаторами
     */
    public static Map<String, String> initParentDatasourceIdsMap(PageContext context, CompileProcessor p) {
        Map<String, String> parentDatasourceIdsMap = new HashMap<>();

        DataSourcesScope scope = p.getScope(DataSourcesScope.class);
        if (!CollectionUtils.isEmpty(scope)) {
            for (Map.Entry<String, N2oAbstractDatasource> entry : scope.entrySet()) {
                if (!(entry.getValue() instanceof N2oParentDatasource parentDatasource)) {
                    parentDatasourceIdsMap.put(entry.getKey(), getClientDatasourceId(entry.getKey(), p));
                } else if (!parentDatasource.isFromParentPage()) {
                    parentDatasourceIdsMap.put(entry.getKey(), context.getParentDatasourceIdsMap().get(entry.getKey()));
                }
            }
        }

        return parentDatasourceIdsMap;
    }

    /**
     * Инициализация map моделей по имени параметра из списка параметров.
     *
     * @param params     Список параметров
     * @param pathParams Map моделей по имени параметра.
     *                   Используется для фильтрации параметров, не входящих в данную map
     * @return Map моделей по имени параметра
     */
    private static Map<String, ModelLink> initParams(List<N2oParam> params,
                                                     Map<String, ModelLink> pathParams,
                                                     CompileProcessor p) {
        if (params == null)
            return null;

        return params.stream()
                .filter(f -> f.getName() != null && !pathParams.containsKey(f.getName()))
                .collect(Collectors.toMap(N2oParam::getName, param -> {
                    ModelLink link = Redux.linkParam(param, p);
                    String datasource = param.getDatasourceId();
                    DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
                    if (ReduxModelEnum.RESOLVE.equals(link.getModel()) && Objects.equals(link.getFieldId(), "id")
                            && dataSourcesScope.get(datasource) instanceof N2oStandardDatasource sd) {
                        link.setSubModelQuery(new SubModelQuery(sd.getQueryId()));
                    }
                    return link;
                }));
    }
}
