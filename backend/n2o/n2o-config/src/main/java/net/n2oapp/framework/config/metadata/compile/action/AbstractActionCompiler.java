package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil.getClientWidgetIdByComponentScope;
import static net.n2oapp.framework.config.util.CompileUtil.getClientDatasourceId;

/**
 * Абстрактная реализация компиляции действия
 */
public abstract class AbstractActionCompiler<D extends Action, S extends N2oAction>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    public void compileAction(D compiled, S source, CompileProcessor p) {
        compiled.setProperties(p.mapAttributes(source));
    }

    protected void initDefaults(S source, CompileContext<?, ?> context, CompileProcessor p) {
        source.setId(initId(source, p));
    }

    protected String initId(S source, CompileProcessor p) {
        if (source.getId() == null) {
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope != null) {
                IdAware component = componentScope.unwrap(IdAware.class);
                if (component != null) {
                    return component.getId();
                } else {
                    WidgetScope widgetScope = p.getScope(WidgetScope.class);
                    if (widgetScope != null) {
                        return widgetScope.getClientWidgetId() + "_row";
                    }
                }
            }
        }
        return source.getId();
    }

    /**
     * Инициализация целевого виджета действия
     */
    protected String initClientWidgetId(CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String targetWidgetId = getClientWidgetIdByComponentScope(p); //widget in component's link
        if (targetWidgetId == null) {
            if (widgetScope != null) {
                //current widget
                targetWidgetId = widgetScope.getClientWidgetId();
            } else {
                throw new N2oException("Unknown widgetId for invoke action!");
            }
        }
        return targetWidgetId;
    }

    protected ReduxModel getModelFromComponentScope(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                return modelAware.getModel();
            }
        }
        return ReduxModel.resolve;
    }

    /**
     * Получение текущей страницы
     */
    protected String getPageId(CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null)
            return pageScope.getPageId();
        else
            return null;
    }

    /**
     * Сборка родительских маппингов путей
     *
     * @param routeScope Информация о родительском маршруте
     * @param url        Маршрут
     * @return Map родительских маппингов путей
     */
    protected Map<String, ModelLink> initParentRoutePathMappings(ParentRouteScope routeScope, String url) {
        Map<String, ModelLink> parentRoutePathMappings = new StrictMap<>();
        if (routeScope != null && routeScope.getPathMapping() != null) {
            List<String> pathParams = RouteUtil.getParams(url);
            routeScope.getPathMapping().forEach((k, v) -> {
                if (pathParams.contains(k)) {
                    parentRoutePathMappings.put(k, v);
                }
            });
        }
        return parentRoutePathMappings;
    }

    /**
     * Инициализация маппингов для query и path параметров
     *
     * @param pathParams   Исходные модели параметров пути
     * @param queryParams  Исходные модели параметров запроса
     * @param pathMapping  Map, в которую будет произведена сборка собранных параметров пути
     * @param queryMapping Map, в которую будет произведена сборка собранных параметров запроса
     * @param p            Процессор сборки метаданных
     */
    protected void initMappings(N2oParam[] pathParams, N2oParam[] queryParams,
                                Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping,
                                CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        ReduxModel defaultModel = getModelFromComponentScope(p);
        if (widgetScope != null) {
            String defaultClientWidgetId = getDefaultClientWidgetId(p);
            if (pathParams != null)
                for (N2oParam pathParam : pathParams)
                    pathMapping.put(pathParam.getName(), initParamModelLink(pathParam, defaultClientWidgetId, defaultModel, p));
            if (queryParams != null)
                for (N2oParam queryParam : queryParams)
                    queryMapping.put(queryParam.getName(), initParamModelLink(queryParam, defaultClientWidgetId, defaultModel, p));
        } else {
            if (pathParams != null)
                for (N2oParam pathParam : pathParams)
                    pathMapping.put(pathParam.getName(), initParamModelLink(pathParam, null, defaultModel, p));
            if (queryParams != null)
                for (N2oParam queryParam : queryParams)
                    queryMapping.put(queryParam.getName(), initParamModelLink(queryParam, null, defaultModel, p));
        }
    }

    /**
     * Инициализация локального источника данных действия
     *
     * @param p Процессор сборки
     * @return Локальный источник данных действия
     */
    protected String getLocalDatasource(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        while (componentScope != null) {
            DatasourceIdAware datasourceIdAware = componentScope.unwrap(DatasourceIdAware.class);
            if (datasourceIdAware != null && datasourceIdAware.getDatasourceId() != null) {
                return datasourceIdAware.getDatasourceId();
            }
            componentScope = componentScope.getParentScope();
        }
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            return widgetScope.getDatasourceId();
        return null;
    }

    /**
     * Инициализация модели ссылки параметра
     *
     * @param param                 Исходная модель параметра
     * @param defaultClientWidgetId Идентификатор виджета по умолчанию
     * @param defaultModel          Модель по умолчанию
     * @param p                     Процессор сборки метаданных
     * @return Модель ссылки параметра
     */
    private ModelLink initParamModelLink(N2oParam param, String defaultClientWidgetId, ReduxModel defaultModel, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        String widgetId = p.cast(getClientDatasourceId(param.getRefWidgetId(), p), defaultClientWidgetId);

        String clientDatasourceId;
        if (pageScope == null) {
            clientDatasourceId = param.getDatasourceId() != null ? param.getDatasourceId() : widgetId;
            if (clientDatasourceId == null)
                clientDatasourceId = getLocalDatasource(p);
        } else {
            if (param.getDatasourceId() != null)
                clientDatasourceId = getClientDatasourceId(param.getDatasourceId(), p);
            else if (widgetId != null)
                clientDatasourceId = pageScope.getWidgetIdClientDatasourceMap().get(widgetId);
            else
                clientDatasourceId = getClientDatasourceId(getLocalDatasource(p), p);
        }

        ModelLink link = new ModelLink(p.cast(param.getModel(), defaultModel), clientDatasourceId);
        link.setValue(p.resolveJS(param.getValue()));
        return link;
    }

    /**
     * Получение идентификатора клиентского виджета по умолчанию
     *
     * @param p           Процессор сборки метаданных
     * @return Идентификатор клиентского виджета по умолчанию
     */
    private String getDefaultClientWidgetId(CompileProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String widgetIdByComponentScope = getClientWidgetIdByComponentScope(p);
        return widgetIdByComponentScope != null ? widgetIdByComponentScope : widgetScope.getClientWidgetId();
    }
}
