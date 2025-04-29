package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethodEnum;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderUtil.getWidgetIdByComponentScope;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Сборка действия вызова операции
 */
@Component
public class InvokeActionCompiler extends AbstractMetaActionCompiler<InvokeAction, N2oInvokeAction> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInvokeAction.class;
    }

    @Override
    public InvokeAction compile(N2oInvokeAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        InvokeAction invokeAction = new InvokeAction();
        compileAction(invokeAction, source, p);
        invokeAction.setOperationId(source.getOperationId());
        invokeAction.setType(getType(p));

        invokeAction.getPayload().setModel(getModelFromComponentScope(p));
        invokeAction.getPayload().setDatasource(getClientDatasourceId(getLocalDatasourceId(p), p));
        invokeAction.getPayload().setWidgetId(getClientDatasourceId(getWidgetIdByComponentScope(p), p));
        invokeAction.getPayload().setPageId(getPageId(p));

        invokeAction.getMeta().setSuccess(initSuccessMeta(invokeAction, source, context, p));
        invokeAction.getMeta().setFail(initFailMeta(invokeAction, source, context));
        initDataProvider(invokeAction, source, context, p);

        return invokeAction;
    }

    @Override
    protected MetaSaga initSuccessMeta(InvokeAction compiled, N2oInvokeAction source, CompileContext<?, ?> context, CompileProcessor p) {
        MetaSaga metaSaga = super.initSuccessMeta(compiled, source, context, p);
        metaSaga.setMessageWidgetId(
                getMessageWidgetId(compiled, context, source.getCloseOnSuccess() || source.getDoubleCloseOnSuccess())
        );
        initClear(source, p, metaSaga);

        return metaSaga;
    }

    @Override
    protected MetaSaga initFailMeta(InvokeAction compiled, N2oInvokeAction source, CompileContext<?, ?> context) {
        MetaSaga metaSaga = super.initFailMeta(compiled, source, context);
        metaSaga.setMessageWidgetId(compiled.getPayload().getWidgetId());

        return metaSaga;
    }

    private String getType(CompileProcessor p) {
        return p.resolve(property("n2o.api.action.invoke.type"), String.class);
    }

    protected void initDefaults(N2oInvokeAction source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        source.setRoute(castDefault(RouteUtil.normalize(source.getRoute()), "/" + source.getId()));
        initSubmitMessageDefaults(source);
        source.setOptimistic(
                castDefault(source.getOptimistic(),
                        () -> p.resolve(property("n2o.api.action.invoke.optimistic"), Boolean.class))
        );
        source.setSubmitAll(castDefault(source.getSubmitAll(), true));
        source.setMethod(
                castDefault(source.getMethod(),
                        () -> p.resolve(property("n2o.api.action.invoke.method"), RequestMethodEnum.class))
        );
        source.setClearOnSuccess(castDefault(source.getClearOnSuccess(), false));
    }

    private void initSubmitMessageDefaults(N2oInvokeAction source) {
        source.setMessageOnSuccess(castDefault(source.getMessageOnSuccess(), true));
        source.setMessageOnFail(castDefault(source.getMessageOnFail(), true));
        source.setUseFailOut(castDefault(source.getUseFailOut(), true));
    }

    private String getMessageWidgetId(InvokeAction compiled, CompileContext<?, ?> context, boolean closeOnSuccess) {
        if (closeOnSuccess && (context instanceof PageContext pageContext)) {
            return pageContext.getParentClientWidgetId();
        }

        return compiled.getPayload().getWidgetId();
    }


    private void initDataProvider(InvokeAction invokeAction, N2oInvokeAction source,
                                  CompileContext<?, ?> context, CompileProcessor p) {
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setId(source.getId());
        dataProvider.setOptimistic(source.getOptimistic());
        dataProvider.setTargetModel(getModelFromComponentScope(p));
        dataProvider.setDatasourceId(getLocalDatasourceId(p));
        dataProvider.setClientDatasourceId(getClientDatasourceId(dataProvider.getDatasourceId(), p));
        validatePathAndRoute(source, routeScope);
        dataProvider.setPathParams(source.getPathParams());
        dataProvider.setFormParams(source.getFormParams());
        dataProvider.setHeaderParams(source.getHeaderParams());
        dataProvider.setMethod(source.getMethod());
        dataProvider.setUrl(RouteUtil.normalize(source.getRoute()));
        dataProvider.setSubmitForm(source.getSubmitAll());

        CompiledObject compiledObject = getObject(source, p);
        invokeAction.setObjectId(compiledObject.getId());

        AsyncMetaSaga metaSaga = invokeAction.getMeta();
        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(compiledObject.getId());
        actionContextData.setOperationId(source.getOperationId());
        checkOperationIdExistence(actionContextData, compiledObject);
        actionContextData.setClearDatasource(metaSaga.getSuccess().getClear());
        actionContextData.setRedirect(initServerRedirect(metaSaga));
        actionContextData.setRefresh(metaSaga.getSuccess().getRefresh());
        actionContextData.setParentWidgetId(metaSaga.getSuccess().getMessageWidgetId());
        actionContextData.setMessagesForm(metaSaga.getFail().getMessageWidgetId());
        actionContextData.setMessageOnSuccess(source.getMessageOnSuccess());
        actionContextData.setMessageOnFail(source.getMessageOnFail());
        actionContextData.setUseFailOut(source.getUseFailOut());
        actionContextData.setMessagePosition(source.getMessagePosition());
        actionContextData.setMessagePlacement(source.getMessagePlacement());
        actionContextData.setOperation(compiledObject.getOperations().get(source.getOperationId()));
        dataProvider.setActionContextData(actionContextData);
        ClientDataProvider compiledDataProvider = ClientDataProviderUtil.compile(dataProvider, context, p);
        if (nonNull(routeScope) && nonNull(compiledDataProvider.getPathMapping())) {
            compiledDataProvider.getPathMapping().putAll(routeScope.getPathMapping());
        }
        invokeAction.getPayload().setDataProvider(compiledDataProvider);
    }

    /**
     * Метод поиска CompiledObject, который может лежать в контексте, либо в локальном datasource, либо в query, либо в методанных.
     * В случае отсутствия бросается исключение N2oException
     *
     * @param source - модель действия вызова операции
     * @param p      - процессор сборки метаданных
     * @return скомилированный не нулевой объект
     */
    private CompiledObject getObject(N2oInvokeAction source, CompileProcessor p) {
        if (nonNull(source.getObjectId()))
            return p.getCompiled(new ObjectContext(source.getObjectId()));

        CompiledObject compiledObject = null;
        String localDatasource = getLocalDatasourceId(p);
        DataSourcesScope dataSourcesScope = p.getScope(DataSourcesScope.class);
        if (nonNull(localDatasource)
                && nonNull(dataSourcesScope)
                && dataSourcesScope.get(localDatasource) instanceof N2oStandardDatasource datasource) {
            String objectId = datasource.getObjectId();
            if (nonNull(objectId)) {
                return p.getCompiled(new ObjectContext(objectId));
            }
            if (nonNull(datasource.getQueryId())) {
                compiledObject = p.getCompiled(new QueryContext(datasource.getQueryId())).getObject();
            }
        }
        if (isNull(compiledObject))
            compiledObject = p.getScope(CompiledObject.class);
        if (isNull(compiledObject))
            throw new N2oException(String.format("В действии \"<invoke>\" не указан идентификатор объекта 'object-id' для операции '%s'", source.getOperationId()));

        return compiledObject;
    }

    private void validatePathAndRoute(N2oInvokeAction source, ParentRouteScope routeScope) {
        String route = source.getRoute();
        N2oParam[] pathParams = source.getPathParams();
        List<String> routeParams = isNull(route) ? null : RouteUtil.getParams(route);
        if ((isNull(routeParams) || routeParams.isEmpty()) && (isNull(pathParams) || pathParams.length == 0))
            return;

        if (isNull(routeParams) || routeParams.isEmpty())
            throw new N2oException(String.format("Параметр пути '%s' не используется в маршруте", pathParams[0].getName()));
        if (isNull(pathParams) || pathParams.length == 0)
            throw new N2oException(String.format("Параметр пути '%s' для маршрута '%s' не установлен", route, routeParams.get(0)));

        for (N2oParam pathParam : pathParams) {
            String paramName = pathParam.getName();
            if (routeParams.stream().noneMatch(p -> p.equals(paramName)))
                throw new N2oException(String.format("Маршрут '%s' не содержит параметр пути '%s'", route, paramName));
            if (nonNull(routeScope.getUrl()) && RouteUtil.getParams(routeScope.getUrl()).contains(paramName))
                throw new N2oException(String.format("Параметр пути '%s' дублируется в родительском 'url'", paramName));
        }
    }

    private void checkOperationIdExistence(N2oClientDataProvider.ActionContextData actionContextData, CompiledObject compiledObject) {
        String operationId = actionContextData.getOperationId();
        if (compiledObject.getOperations().keySet().stream().noneMatch(k -> k.equals(operationId)))
            throw new N2oException(
                    String.format("Действие \"<invoke>\" ссылается на несуществующую операцию 'operation-id = %s' объекта '%s'",
                            operationId,
                            compiledObject.getId())
            );
    }

    private void initClear(N2oInvokeAction source, CompileProcessor p, MetaSaga meta) {
        if (source.getClearOnSuccess())
            meta.setClear(getClientDatasourceId(getLocalDatasourceId(p), p));
    }

    private RedirectSaga initServerRedirect(AsyncMetaSaga meta) {
        boolean isServer = nonNull(meta)
                && nonNull(meta.getSuccess())
                && nonNull(meta.getSuccess().getRedirect())
                && meta.getSuccess().getRedirect().isServer();

        return isServer ? meta.getSuccess().getRedirect() : null;
    }
}
