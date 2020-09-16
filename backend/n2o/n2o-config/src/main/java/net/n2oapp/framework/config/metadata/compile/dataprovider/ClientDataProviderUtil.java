package net.n2oapp.framework.config.metadata.compile.dataprovider;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Утилита для компиляции провайдера данных клиента
 */
public class ClientDataProviderUtil {

    public static ClientDataProvider compile(N2oClientDataProvider compiled, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String path = null;
        String targetWidget = compiled.getTargetWidgetId() == null ? initTargetWidget(context, p) : compiled.getTargetWidgetId();
        ReduxModel targetModel = initTargetWidgetModel(p, compiled.getTargetModel());

        if (RequestMethod.POST == compiled.getMethod()) {
            Map<String, ModelLink> pathMapping = new StrictMap<>();
            pathMapping.putAll(compileParams(compiled.getPathParams(), p, targetModel, targetWidget));
            dataProvider.setFormMapping(compileParams(compiled.getFormParams(), p, targetModel, targetWidget));
            dataProvider.setHeadersMapping(compileParams(compiled.getHeaderParams(), p, targetModel, targetWidget));
            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            path = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute((N2oCompileProcessor) p), "");
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (widgetScope != null) {
                String clientWidgetId = widgetScope.getClientWidgetId();
                if (ReduxModel.RESOLVE.equals(targetModel)) {
                    String widgetSelectedId = clientWidgetId + "_id";
                    //todo не нужно добавлять принудительно параметр в url, нужно только если его задали в route="/:id/action"
                    path = normalize(path + normalize(colon(widgetSelectedId)));
                    pathMapping.put(widgetSelectedId, new ModelLink(targetModel, clientWidgetId, "id"));
                    if (context.getPathRouteMapping() != null)
                        pathMapping.putAll(context.getPathRouteMapping());
                }
            }
            path = normalize(path + normalize(p.cast(compiled.getUrl(), compiled.getId(), "")));
            dataProvider.setPathMapping(pathMapping);
            dataProvider.setMethod(RequestMethod.POST);
            dataProvider.setOptimistic(compiled.getOptimistic());
            dataProvider.setSubmitForm(compiled.getSubmitForm());

            initActionContext(compiled, pathMapping, p.cast(path, compiled.getUrl()), p);
        }

        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + p.cast(path, compiled.getUrl()));
        dataProvider.setQueryMapping(compileParams(compiled.getQueryParams(), p, targetModel, targetWidget));
        dataProvider.setQuickSearchParam(compiled.getQuickSearchParam());

        return dataProvider;
    }

    private static Map<String, ModelLink> compileParams(N2oParam[] params, CompileProcessor p, ReduxModel model,
                                                        String targetWidgetId) {
        if (params == null)
            return Collections.emptyMap();
        Map<String, ModelLink> result = new StrictMap<>();
        for (N2oParam param : params) {
            ModelLink link;
            Object value = param.getValueList() != null ? param.getValueList() :
                    ScriptProcessor.resolveExpression(param.getValue());
            if (value == null || StringUtils.isJs(value)) {
                String widgetId = null;
                if (param.getRefWidgetId() != null) {
                    String pageId = param.getRefPageId();
                    if (param.getRefPageId() == null) {
                        PageScope pageScope = p.getScope(PageScope.class);
                        if (pageScope != null)
                            pageId = pageScope.getPageId();
                    }
                    widgetId = CompileUtil.generateWidgetId(pageId, param.getRefWidgetId());
                }
                link = new ModelLink(p.cast(param.getRefModel(), model), p.cast(widgetId, targetWidgetId));
                link.setValue(value);
            } else {
                link = new ModelLink(value);
            }
            result.put(param.getName(), link);
        }
        return result;
    }

    private static void initActionContext(N2oClientDataProvider source, Map<String, ModelLink> pathMapping,
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
            ValidationList validationList = p.getScope(ValidationList.class);
            actionContext.setValidations(validationList == null ? null : validationList.get(actionContextData.getFailAlertWidgetId(),
                    initTargetWidgetModel(p, source.getTargetModel())));
            actionContext.setRedirect(actionContextData.getRedirect());
            actionContext.setParentWidgetId(actionContextData.getParentWidgetId());
            actionContext.setFailAlertWidgetId(actionContextData.getFailAlertWidgetId());
            actionContext.setMessagesForm(actionContextData.getMessagesForm());
            actionContext.setSuccessAlertWidgetId(actionContextData.getSuccessAlertWidgetId());
            actionContext.setMessageOnSuccess(actionContextData.isMessageOnSuccess());
            actionContext.setMessageOnFail(actionContextData.isMessageOnFail());

            Set<String> formParams = p.getScope(CompiledObject.class).getOperations()
                    .get(actionContext.getOperationId()).getFormParams();
            if (source.getFormParams() != null)
                Arrays.stream(source.getFormParams()).forEach(fp -> formParams.add(fp.getId()));

            Map<String, String> operationMapping = new StrictMap<>();
            for (N2oObject.Parameter inParameter : actionContextData.getOperation().getInParametersMap().values()) {
                String param = inParameter.getParam();
                // form params should be ignored in operationMapping
                if (param != null && !formParams.contains(param))
                    operationMapping.put(param, inParameter.getId());
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
    private static ReduxModel initTargetWidgetModel(CompileProcessor p, ReduxModel defaultModel) {
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
