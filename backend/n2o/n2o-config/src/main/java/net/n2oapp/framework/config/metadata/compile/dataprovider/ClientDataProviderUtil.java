package net.n2oapp.framework.config.metadata.compile.dataprovider;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ValidateType;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.CompileUtil;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Утилита для компиляции провайдера данных клиента
 */
public class ClientDataProviderUtil {

    private ClientDataProviderUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static ClientDataProvider compile(N2oClientDataProvider compiled, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String path = null;
        String targetWidget = compiled.getTargetWidgetId() == null ? initTargetWidget(context, p) : compiled.getTargetWidgetId();
        ReduxModel targetModel = getTargetActionModel(p, compiled.getTargetModel());

        if (RequestMethod.POST == compiled.getMethod() ||
                RequestMethod.PUT == compiled.getMethod() ||
                RequestMethod.DELETE == compiled.getMethod()) {
            Map<String, ModelLink> pathMapping = new StrictMap<>();
            pathMapping.putAll(compileParams(compiled.getPathParams(), context, p, targetModel, targetWidget));
            dataProvider.setFormMapping(compileParams(compiled.getFormParams(), context, p, targetModel, targetWidget));
            dataProvider.setHeadersMapping(compileParams(compiled.getHeaderParams(), context, p, targetModel, targetWidget));
            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            path = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute((N2oCompileProcessor) p), "");
            if (context.getPathRouteMapping() != null)
                pathMapping.putAll(context.getPathRouteMapping());
            path = normalize(path + normalize(p.cast(compiled.getUrl(), compiled.getId(), "")));
            dataProvider.setPathMapping(pathMapping);
            dataProvider.setMethod(compiled.getMethod());
            dataProvider.setOptimistic(compiled.getOptimistic());
            dataProvider.setSubmitForm(compiled.getSubmitForm());

            initActionContext(compiled, pathMapping, p.cast(path, compiled.getUrl()), p);
        }

        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + p.cast(path, compiled.getUrl()));
        dataProvider.setQueryMapping(compileParams(compiled.getQueryParams(), context, p, targetModel, targetWidget));
        dataProvider.setQuickSearchParam(compiled.getQuickSearchParam());
        dataProvider.setSize(compiled.getSize());

        return dataProvider;
    }

    public static String getDatasourceByComponentScope(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            DatasourceIdAware datasourceIdAware = componentScope.unwrap(DatasourceIdAware.class);
            if (datasourceIdAware != null && datasourceIdAware.getDatasource() != null) {
                return datasourceIdAware.getDatasource();
            }
        }
        return null;
    }

    public static String getWidgetIdByComponentScope(CompileProcessor p) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                return widgetIdAware.getWidgetId();
            }
        }
        return null;
    }

    public static String getClientWidgetIdByComponentScope(CompileProcessor p) {
        String widgetId = getWidgetIdByComponentScope(p);
        PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null && widgetId != null) {
                return pageScope.getGlobalWidgetId(widgetId);
            }
        return null;
    }

    private static Map<String, ModelLink> compileParams(N2oParam[] params, CompileContext<?, ?> context,
                                                        CompileProcessor p, ReduxModel model, String targetWidgetId) {
        if (params == null)
            return Collections.emptyMap();
        Map<String, ModelLink> result = new StrictMap<>();
        for (N2oParam param : params) {
            ModelLink link;
            if (param.getValueParam() == null) {
                link = getModelLink(p, model, targetWidgetId, param);
            } else {
                link = getModelLinkByParam(context, param);
            }
            if (link != null)
                result.put(param.getName(), link);
        }
        return result;
    }

    private static ModelLink getModelLink(CompileProcessor p, ReduxModel model, String targetWidgetId, N2oParam param) {
        ModelLink link;
        Object value = param.getValueList() != null ? param.getValueList() :
                ScriptProcessor.resolveExpression(param.getValue());
        if (value == null || StringUtils.isJs(value)) {
            String widgetId = null;
            PageScope pageScope = p.getScope(PageScope.class);
            if (param.getRefWidgetId() != null) {
                String pageId = param.getRefPageId();
                if (param.getRefPageId() == null && pageScope != null)
                        pageId = pageScope.getPageId();
                widgetId = CompileUtil.generateWidgetId(pageId, param.getRefWidgetId());
            }
            String resultWidgetId = p.cast(widgetId, targetWidgetId);
            String datasourceId = pageScope == null || pageScope.getWidgetIdClientDatasourceMap() == null
                    ? resultWidgetId : pageScope.getWidgetIdClientDatasourceMap().get(resultWidgetId);
            link = new ModelLink(p.cast(param.getModel(), model), datasourceId);
            link.setValue(value);
        } else {
            link = new ModelLink(value);
        }
        return link;
    }

    private static ModelLink getModelLinkByParam(CompileContext<?, ?> context, N2oParam param) {
        ModelLink link = null;
        if (context.getPathRouteMapping() != null && context.getPathRouteMapping().containsKey(param.getValueParam())) {
            link = context.getPathRouteMapping().get(param.getValueParam());
            link.setParam(param.getValueParam());
        } else if (context.getQueryRouteMapping() != null && context.getQueryRouteMapping().containsKey(param.getValueParam())) {
            link = context.getQueryRouteMapping().get(param.getValueParam());
            link.setParam(param.getValueParam());
        } else {
            link = new ModelLink();
            link.setParam(param.getValueParam());
        }
        return link;
    }

    public static void initActionContext(N2oClientDataProvider source, Map<String, ModelLink> pathMapping,
                                          String url, CompileProcessor p) {
        if (source.getActionContextData() != null) {
            N2oClientDataProvider.ActionContextData actionContextData = source.getActionContextData();
            ActionContext actionContext = new ActionContext(actionContextData.getObjectId(), actionContextData.getOperationId(), url);

            Map<String, ModelLink> routePathMapping = new StrictMap<>();
            Map<String, ModelLink> routeQueryMapping = new StrictMap<>();

            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            if (routeScope != null) {
                routePathMapping.putAll(routeScope.getPathMapping());
                routePathMapping.putAll(pathMapping);
                routeQueryMapping.putAll(routeScope.getQueryMapping());
            }
            actionContext.setPathRouteMapping(routePathMapping);
            actionContext.setQueryRouteMapping(routeQueryMapping);
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope == null || componentScope.unwrap(N2oButton.class) == null) {
                ValidationList validationList = p.getScope(ValidationList.class);
                if (validationList != null)
                    actionContext.setValidations(validationList.get(source.getDatasourceId(), getTargetActionModel(p, source.getTargetModel())));
            }
            actionContext.setRedirect(actionContextData.getRedirect());
            actionContext.setRefresh(actionContextData.getRefresh());
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null)
                actionContext.setParentPageId(pageScope.getPageId());
            actionContext.setParentWidgetId(actionContextData.getParentWidgetId());
            actionContext.setFailAlertWidgetId(actionContextData.getFailAlertWidgetId());
            actionContext.setMessagesForm(actionContextData.getMessagesForm());
            actionContext.setSuccessAlertWidgetId(actionContextData.getSuccessAlertWidgetId());
            actionContext.setMessageOnSuccess(actionContextData.isMessageOnSuccess());
            actionContext.setMessageOnFail(p.cast(actionContextData.isMessageOnFail(), true));
            actionContext.setMessagePosition(p.cast(actionContextData.getMessagePosition(), MessagePosition.fixed));//todo initDefaults
            actionContext.setMessagePlacement(p.cast(actionContextData.getMessagePlacement(), MessagePlacement.top));//todo initDefaults

            Set<String> formParams = new HashSet<>();
            if (source.getFormParams() != null)
                Arrays.stream(source.getFormParams()).forEach(fp -> formParams.add(fp.getId()));

            Map<String, String> operationMapping = new StrictMap<>();
            for (AbstractParameter inParameter : actionContextData.getOperation().getInParametersMap().values()) {
                if (inParameter instanceof ObjectSimpleField) {
                    String param = ((ObjectSimpleField) inParameter).getParam();
                    // form params from this source should be ignored in operationMapping
                    if (param != null && !formParams.contains(param))
                        operationMapping.put(param, inParameter.getId());
                }
            }
            actionContext.setOperationMapping(operationMapping);
            p.addRoute(actionContext);
        }
    }

    /**
     * Инициализация целевого виджета
     */
    private static String initTargetWidget(CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        String targetWidgetId = null;
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                targetWidgetId = pageScope == null ?
                        widgetIdAware.getWidgetId() : pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());//todo обсудить
            }
        }
        if (targetWidgetId == null) {
            if (widgetScope != null) {
                targetWidgetId = widgetScope.getClientWidgetId();
            } else if (context instanceof PageContext && ((PageContext) context).getResultWidgetId() != null && pageScope != null) {
                targetWidgetId = pageScope.getGlobalWidgetId(((PageContext) context).getResultWidgetId());
            } else {
                throw new N2oException("Unknown widgetId for invoke action!");
            }
        }
        return targetWidgetId;
    }

    /**
     * Инициализация модели целевого виджета
     */
    private static ReduxModel getTargetActionModel(CompileProcessor p, ReduxModel defaultModel) {
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                return modelAware.getModel();
            }
        }
        return defaultModel;
    }

    public static String initClientDatasource(String datasourceId, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null && datasourceId != null)
            return pageScope.getClientDatasourceId(datasourceId);
        return null;
    }
}
