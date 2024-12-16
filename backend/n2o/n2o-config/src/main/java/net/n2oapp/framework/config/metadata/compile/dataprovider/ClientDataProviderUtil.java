package net.n2oapp.framework.config.metadata.compile.dataprovider;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.*;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Утилита для компиляции провайдера данных клиента
 */
public class ClientDataProviderUtil {

    private ClientDataProviderUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static ClientDataProvider compile(N2oClientDataProvider source, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String path = null;
        ReduxModel targetModel = getTargetActionModel(p, source.getTargetModel());

        if (RequestMethod.POST == source.getMethod() ||
                RequestMethod.PUT == source.getMethod() ||
                RequestMethod.DELETE == source.getMethod()) {
            Map<String, ModelLink> pathMapping = new HashMap<>();
            pathMapping.putAll(compileParams(source.getPathParams(), context, p, targetModel, source.getClientDatasourceId()));
            dataProvider.setFormMapping(compileParams(source.getFormParams(), context, p, targetModel, source.getClientDatasourceId()));
            dataProvider.setHeadersMapping(compileParams(source.getHeaderParams(), context, p, targetModel, source.getClientDatasourceId()));
            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            path = castDefault(routeScope != null ? routeScope.getUrl() : null, () -> context.getRoute((N2oCompileProcessor) p), () -> "");
            if (context.getPathRouteMapping() != null)
                pathMapping.putAll(context.getPathRouteMapping());
            path = normalize(path + normalize(castDefault(source.getUrl(), source.getId(), "")));
            dataProvider.setPathMapping(pathMapping);
            dataProvider.setMethod(source.getMethod());
            dataProvider.setOptimistic(source.getOptimistic());
            dataProvider.setSubmitForm(source.getSubmitForm());

            initActionContext(source, pathMapping, castDefault(path, source.getUrl()), p);
        }

        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + castDefault(path, source.getUrl()));
        dataProvider.setQueryMapping(compileParams(source.getQueryParams(), context, p, targetModel, source.getClientDatasourceId()));
        dataProvider.setQuickSearchParam(source.getQuickSearchParam());
        dataProvider.setSize(source.getSize());

        return dataProvider;
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

    private static Map<String, ModelLink> compileParams(N2oParam[] params, CompileContext<?, ?> context,
                                                        CompileProcessor p, ReduxModel defaultModel,
                                                        String defaultClientDatasourceId) {
        if (params == null)
            return Collections.emptyMap();
        Map<String, ModelLink> result = new LinkedHashMap<>();
        for (N2oParam param : params) {
            ModelLink link;
            if (param.getValueParam() == null) {
                link = getModelLink(p,
                        castDefault(param.getModel(), defaultModel),
                        castDefault(param.getDatasourceId(), defaultClientDatasourceId),
                        param);
            } else {
                link = getModelLinkByParam(context, param);
            }
            if (param.getRequired() != null && param.getRequired()) {
                link.setRequired(true);
            }
            result.put(param.getName(), link);
        }
        return result;
    }

    private static ModelLink getModelLink(CompileProcessor p, ReduxModel model, String defaultClientDatasourceId, N2oParam param) {
        ModelLink link;
        Object value = param.getValueList() != null ? param.getValueList() :
                ScriptProcessor.resolveExpression(param.getValue());
        if (value == null || StringUtils.isJs(value)) {
            String clientDatasourceId;
            if (param.getDatasourceId() == null) {
                clientDatasourceId = defaultClientDatasourceId;
            } else {
                clientDatasourceId = param.getRefPageId() != null ?
                        getClientDatasourceId(param.getDatasourceId(), param.getRefPageId(), p) :
                        getClientDatasourceId(param.getDatasourceId(), p);
            }
            link = new ModelLink(castDefault(param.getModel(), model), clientDatasourceId);
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

            Map<String, ModelLink> routePathMapping = new HashMap<>();
            Map<String, ModelLink> routeQueryMapping = new LinkedHashMap<>();

            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            if (routeScope != null) {
                routePathMapping.putAll(routeScope.getPathMapping());
                routePathMapping.putAll(pathMapping);
                routeQueryMapping.putAll(routeScope.getQueryMapping());
            }
            actionContext.setPathRouteMapping(routePathMapping);
            actionContext.setQueryRouteMapping(routeQueryMapping);
            ComponentScope componentScope = p.getScope(ComponentScope.class);
            if (componentScope == null
                    || componentScope.unwrap(N2oButton.class) == null
                    || componentScope.unwrap(N2oButton.class).getValidate()) {
                ValidationScope validationScope = p.getScope(ValidationScope.class);
                if (validationScope != null)
                    actionContext.setValidations(validationScope.get(source.getDatasourceId(), getTargetActionModel(p, source.getTargetModel())));
            }

            actionContext.setRedirect(actionContextData.getRedirect());
            actionContext.setRefresh(actionContextData.getRefresh());
            actionContext.setLoading(actionContextData.getLoading());
            actionContext.setClearDatasource(actionContextData.getClearDatasource());
            if (actionContextData.getPolling() != null) {
                actionContext.setPolling(actionContextData.getPolling());
                actionContext.setPollingEndCondition(actionContextData.getPolling().getResult());
            }

            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null)
                actionContext.setParentPageId(pageScope.getPageId());
            actionContext.setParentClientWidgetId(actionContextData.getParentWidgetId());
            actionContext.setParentSourceDatasourceId(source.getDatasourceId());
            actionContext.setMessagesForm(actionContextData.getMessagesForm());
            actionContext.setMessageOnSuccess(actionContextData.isMessageOnSuccess());
            actionContext.setMessageOnFail(castDefault(actionContextData.isMessageOnFail(), true));
            actionContext.setUseFailOut(actionContextData.isUseFailOut());
            actionContext.setMessagePosition(castDefault(actionContextData.getMessagePosition(),
                    () -> p.resolve(property("n2o.api.message.position"), MessagePosition.class)));
            actionContext.setMessagePlacement(castDefault(actionContextData.getMessagePlacement(),
                    () -> p.resolve(property("n2o.api.message.placement"), MessagePlacement.class)));

            Set<String> formParams = new HashSet<>();
            if (source.getFormParams() != null)
                Arrays.stream(source.getFormParams()).forEach(fp -> formParams.add(fp.getName()));

            Map<String, String> operationMapping = new HashMap<>();
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
}
