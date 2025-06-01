package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.action.*;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.PageRefEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.DynamicUtil.hasRefs;
import static net.n2oapp.framework.api.DynamicUtil.isDynamic;
import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.PageContextCompileUtil.initMapping;
import static net.n2oapp.framework.config.util.PageContextCompileUtil.initParentDatasourceIdsMap;

/**
 * Абстрактная реализация компиляция open-page, show-modal
 */
@Component
public abstract class AbstractOpenPageCompiler<D extends Action, S extends N2oAbstractPageAction> extends AbstractActionCompiler<D, S> {
    private static final String WIDGET_ID = "$widgetId";

    @Override
    protected void initDefaults(S source, CompileProcessor p) {
        super.initDefaults(source, p);

        if (source.getDatasources() != null) {
            Stream.of(source.getDatasources())
                    .filter(N2oStandardDatasource.class::isInstance)
                    .forEach(ds -> initDefaultsDatasource((N2oStandardDatasource) ds, p));
        }
        if (source.getParams() != null) {
            Stream.of(source.getParams()).forEach(param -> initDefaultsParam(param, p, source));
        }
    }

    private void initDefaultsParam(N2oParam param, CompileProcessor p, S source) {
        param.setModel(castDefault(param.getModel(), () -> getModelFromComponentScope(p)));
        param.setDatasourceId(castDefault(param.getDatasourceId(), () -> getLocalDatasourceId(p)));
        if (param.getDatasourceId() == null && param.getValue() == null) {
            throw new N2oException(String.format("Источник данных не определен для параметра %s действия %s", param.getName(), source.getId()));
        }
        param.setRefPageId(castDefault(param.getRefPageId(), () -> {
            PageScope pageScope = p.getScope(PageScope.class);
            if (pageScope != null)
                return pageScope.getPageId();
            return null;
        }));
        if (param.getName() != null && param.getName().contains(WIDGET_ID)) {
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            String clientWidgetId = widgetScope != null ? widgetScope.getClientWidgetId() : "";
            param.setName(param.getName().replace(WIDGET_ID, clientWidgetId));//todo убрать после удаления adaptV1
        }
    }

    /**
     * Приведение поле источника данных к значениям по умолчанию
     *
     * @param datasource Источник данных
     * @param p          Процессор сборки
     */
    protected void initDefaultsDatasource(N2oStandardDatasource datasource, CompileProcessor p) {
        if (datasource.getFilters() == null)
            return;

        PageScope pageScope = p.getScope(PageScope.class);
        String pageId = pageScope == null ? null : pageScope.getPageId();
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        ReduxModelEnum modelFromComponentScope = getModelFromComponentScope(p);
        String localDatasourceId = getLocalDatasourceId(p);
        String clientWidgetId = widgetScope != null ? widgetScope.getClientWidgetId() : "";
        for (N2oPreFilter filter : datasource.getFilters()) {
            filter.setModel(castDefault(filter.getModel(), modelFromComponentScope));
            filter.setDatasourceId(castDefault(filter.getDatasourceId(), localDatasourceId));
            filter.setRefPageId(castDefault(filter.getRefPageId(), pageId));
            if (filter.getParam() != null && filter.getParam().contains(WIDGET_ID)) {
                //todo убрать после удаления adaptV1
                filter.setParam(filter.getParam().replace(WIDGET_ID, clientWidgetId));
            }
        }
    }

    protected PageContext constructContext(S source, String route, CompileProcessor p) {
        PageContext pageContext = new PageContext(source.getPageId(), route);
        initToolbarBySubmitOperation(source, pageContext, p);
        return pageContext;
    }

    protected PageContext initPageContext(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p) {
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        validatePathAndRoute(source.getRoute(), source.getPathParams(), routeScope);
        ReduxModelEnum actionDataModel = getModelFromComponentScope(p);
        PageScope pageScope = p.getScope(PageScope.class);
        String route = castDefault(routeScope != null ? routeScope.getUrl() : null,
                () -> context.getRoute((N2oCompileProcessor) p),
                () -> "");

        Map<String, ModelLink> pathMapping = new HashMap<>();
        Map<String, ModelLink> queryMapping = new LinkedHashMap<>();
        if (routeScope != null) {
            pathMapping.putAll(routeScope.getPathMapping());
            queryMapping.putAll(routeScope.getQueryMapping());
        }

        String currentClientWidgetId = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope != null)
            currentClientWidgetId = widgetScope.getClientWidgetId();

        ComponentScope componentScope = p.getScope(ComponentScope.class);
        ModelLink actionModelLink = createActionModelLink(actionDataModel, currentClientWidgetId,
                componentScope, source.getPageId(), p);

        initMapping(source.getPathParams(), pathMapping, pathMapping, p);

        String actionRoute = initActionRoute(source, actionModelLink, pathMapping);
        String parentRoute = normalize(route);
        route = normalize(route + actionRoute);
        PageContext pageContext = constructContext(source, route, p);
        if (pageScope != null && pageScope.getTabIds() != null)
            pageContext.setParentTabIds(pageScope.getTabIds());

        pageContext.setPageName(source.getPageName());
        pageContext.setBreadcrumbs(initBreadcrumb(source, pageContext, p));
        if (source.getDatasources() != null) {
            if (pageContext.getDatasources() == null)
                pageContext.setDatasources(new ArrayList<>());
            pageContext.getDatasources().addAll(Arrays.asList(source.getDatasources()));
        }
        if (source.getToolbars() != null)
            pageContext.setToolbars(new ArrayList<>(List.of(source.getToolbars())));
        if (source.getActions() != null)
            pageContext.setActions(Arrays.stream(source.getActions())
                    .collect(Collectors.toMap(ActionBar::getId, Function.identity())));

        pageContext.setParentClientWidgetId(currentClientWidgetId);
        String localDatasourceId = getLocalDatasourceId(p);
        pageContext.setParentLocalDatasourceId(localDatasourceId);
        pageContext.setParentClientPageId(pageScope == null ? null : pageScope.getPageId());
        pageContext.setParentRoute(RouteUtil.addQueryParams(parentRoute, queryMapping));
        if (context instanceof PageContext ctx) {
            pageContext.addParentRoute(pageContext.getParentRoute(), ctx);
            pageContext.setParentDatasourceIdsMap(initParentDatasourceIdsMap(ctx, p));
        }
        pageContext.setRefreshOnClose(castDefault(source.getRefreshOnClose(), false));
        if ((!Boolean.FALSE.equals(source.getRefreshAfterSubmit()) || pageContext.getRefreshOnClose())
                && (source.getRefreshDatasourceIds() != null || localDatasourceId != null)) {
            String[] refreshDatasourceIds = source.getRefreshDatasourceIds() == null
                    ? new String[]{localDatasourceId}
                    : source.getRefreshDatasourceIds();
            if (pageScope != null) {
                pageContext.setRefreshClientDataSourceIds(
                        Arrays.stream(refreshDatasourceIds)
                                .map(d -> getClientDatasourceId(d, p))
                                .toList()
                );
            }
        }
        pageContext.setUnsavedDataPromptOnClose(
                castDefault(source.getUnsavedDataPromptOnClose(),
                        () -> p.resolve(property("n2o.api.action.unsaved_data_prompt"), Boolean.class))
        );
        pageContext.setPathRouteMapping(pathMapping);
        initMapping(source.getQueryParams(), pathMapping, queryMapping, p);
        pageContext.setQueryRouteMapping(queryMapping);
        pageContext.setParentModelLinks(collectParentLinks(actionModelLink, pathMapping.values(), queryMapping.values()));

        initPageRoute(compiled, route, pathMapping, queryMapping);
        p.addRoute(pageContext);

        return pageContext;
    }

    private List<Breadcrumb> initBreadcrumb(S source, PageContext pageContext, CompileProcessor p) {
        if (source.getBreadcrumbs() != null) {
            pageContext.setBreadcrumbFromParent(true);
            return Arrays.stream(source.getBreadcrumbs())
                    .map(crumb -> new Breadcrumb(crumb.getLabel(), crumb.getPath()))
                    .toList();
        }

        pageContext.setBreadcrumbFromParent(false);
        return p.getScope(BreadcrumbList.class);
    }

    /**
     * Сбор родительских ссылок на модели в список в порядке приоритета их использования для разрешения
     * параметров открываемой страницы
     *
     * @param actionModelLink Ссылка на модель действия
     * @param pathLinks       Ссылки на модели параметров пути
     * @param queryLinks      Ссылки на модели параметров запроса
     * @return список родительских ссылок
     */
    //TODO убрать в рамках рефакторинга https://jira.i-novus.ru/browse/NNO-8532
    protected List<ModelLink> collectParentLinks(ModelLink actionModelLink, Collection<ModelLink> pathLinks, Collection<ModelLink> queryLinks) {
        List<ModelLink> links = new ArrayList<>();

        links.add(actionModelLink);
        links.addAll(pathLinks);//TODO возможно стоит добавить сортировку по использованию в route
        links.addAll(queryLinks);

        return links;
    }

    /**
     * Создание ссылки на модель действия
     *
     * @param actionDataModel Модель действия
     * @param clientWidgetId  Идентификатор клиентского виджета
     * @param componentScope  Информация о родительском компоненте
     * @param pageId          Идентификатор открываемой страницы
     * @return Ссылка на модель действия
     */
    private ModelLink createActionModelLink(ReduxModelEnum actionDataModel, String clientWidgetId,
                                            ComponentScope componentScope, String pageId, CompileProcessor p) {
        if (componentScope == null)
            return null;

        String datasource;
        DatasourceIdAware datasourceIdAware = componentScope.unwrap(DatasourceIdAware.class);
        if (datasourceIdAware != null && datasourceIdAware.getDatasourceId() != null) {
            datasource = getClientDatasourceId(datasourceIdAware.getDatasourceId(), p);
        } else {
            datasource = clientWidgetId;
        }
        return new ModelLink(actionDataModel, datasource, isLink(pageId) ? unwrapLink(pageId) : QuerySimpleField.PK);
    }

    /**
     * Построение маршрута действия
     *
     * @param source          Действие
     * @param actionModelLink Ссылка на модель действия
     * @param pathMapping     Map моделей параметров пути
     * @return Маршрут действия
     */
    private String initActionRoute(S source, ModelLink actionModelLink, Map<String, ModelLink> pathMapping) {
        String actionRoute = source.getRoute();

        if (actionRoute != null)
            return normalize(actionRoute);

        actionRoute = normalize(source.getId());
        // генерация маршрута для динамической страницы с моделью resolve
        boolean isDynamicPage = hasRefs(source.getPageId()) || isDynamic(source.getPageId());
        if (isDynamicPage && actionModelLink != null && ReduxModelEnum.RESOLVE.equals(actionModelLink.getModel())) {
            String masterIdParam = actionModelLink.getDatasource() + "_id";
            String dynamicPageActionRoute = normalize(colon(masterIdParam)) + actionRoute;
            pathMapping.put(masterIdParam, actionModelLink);

            return dynamicPageActionRoute;
        }

        return actionRoute;
    }

    protected abstract void initPageRoute(D compiled, String route,
                                          Map<String, ModelLink> pathMapping,
                                          Map<String, ModelLink> queryMapping);

    protected void validatePathAndRoute(String route, N2oParam[] pathParams, ParentRouteScope routeScope) {
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
     * Поддержка старых атрибутов submit-operation-id и тд
     */
    @Deprecated
    protected void initToolbarBySubmitOperation(S source, PageContext context, CompileProcessor p) {
        if (!StringUtils.isBlank(source.getSubmitOperationId()) || SubmitActionTypeEnum.COPY.equals(source.getSubmitActionType())) {
            N2oToolbar n2oToolbar = new N2oToolbar();
            if (context.getToolbars() == null) {
                context.setToolbars(new ArrayList<>());
            }
            ToolbarItem[] items = new ToolbarItem[2];
            n2oToolbar.setItems(items);
            context.getToolbars().add(n2oToolbar);

            //create submit button
            N2oButton saveButton = new N2oButton();
            saveButton.setId(GenerateTypeEnum.SUBMIT.getId());
            saveButton.setColor("primary");
            N2oAction[] actions = null;
            ReduxModelEnum saveButtonModel = null;
            SubmitActionTypeEnum submitActionType = castDefault(source.getSubmitActionType(), SubmitActionTypeEnum.INVOKE);
            Boolean closeOnSuccess = castDefault(source.getCloseAfterSubmit(), true);
            Boolean refreshOnSuccessSubmit = castDefault(source.getRefreshAfterSubmit(), true);

            if (submitActionType == SubmitActionTypeEnum.COPY) {
                N2oCopyAction copyAction = new N2oCopyAction();
                copyAction.setSourceModel(source.getCopyModel());
                copyAction.setSourceDatasourceId(source.getCopyDatasourceId());
                copyAction.setSourceFieldId(source.getCopyFieldId());
                copyAction.setTargetModel(source.getTargetModel());
                copyAction.setTargetPage(castDefault(source.getTargetPage(), PageRefEnum.PARENT));
                if (copyAction.getTargetPage().equals(PageRefEnum.PARENT)) {
                    copyAction.setTargetDatasourceId(castDefault(source.getTargetDatasourceId(), () -> getLocalDatasourceId(p)));
                } else {
                    copyAction.setTargetDatasourceId(source.getTargetDatasourceId());
                }
                copyAction.setTargetFieldId(source.getTargetFieldId());
                copyAction.setMode(source.getCopyMode());
                copyAction.setCloseOnSuccess(closeOnSuccess);
                actions = new N2oAction[]{copyAction};
                saveButtonModel = source.getCopyModel();
            } else if (submitActionType == SubmitActionTypeEnum.INVOKE) {
                List<N2oAction> actionList = new ArrayList<>();
                N2oInvokeAction invokeAction = new N2oInvokeAction();
                actionList.add(invokeAction);
                if (refreshOnSuccessSubmit) {
                    if (closeOnSuccess) {
                        invokeAction.setCloseOnSuccess(false);
                        invokeAction.setRefreshOnSuccess(false);
                        String[] refreshDatasourceIds = getRefreshDatasourceId(source, p);
                        for (String refreshDatasourceId : refreshDatasourceIds) {
                            // добавляем refresh action для каждого датасурса
                            N2oRefreshAction refreshAction = new N2oRefreshAction();
                            refreshAction.setDatasourceId("parent_" + refreshDatasourceId);
                            actionList.add(refreshAction);
                            // добавляем parent-datasource чтобы в модалке был этот датасурс
                            if (context.getDatasources() == null)
                                context.setDatasources(new ArrayList<>());
                            N2oParentDatasource parentDatasource = new N2oParentDatasource("parent_" + refreshDatasourceId, refreshDatasourceId, false);
                            context.getDatasources().add(parentDatasource);
                        }
                        N2oCloseAction closeAction = new N2oCloseAction();
                        closeAction.setPrompt(false);
                        actionList.add(closeAction);
                    } else {
                        invokeAction.setRefreshOnSuccess(true);
                        invokeAction.setCloseOnSuccess(false);
                        invokeAction.setRefreshDatasourceIds(source.getRefreshDatasourceIds());
                    }
                } else {
                    invokeAction.setCloseOnSuccess(closeOnSuccess);
                    invokeAction.setRefreshOnSuccess(false);
                }

                if (source.getRedirectUrlAfterSubmit() != null) {
                    invokeAction.setRedirectTarget(castDefault(source.getRedirectTargetAfterSubmit(),
                            () -> (RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? TargetEnum.APPLICATION : TargetEnum.SELF)));
                    invokeAction.setRedirectUrl(source.getRedirectUrlAfterSubmit());
                }

                invokeAction.setOperationId(source.getSubmitOperationId());
                actions = actionList.toArray(new N2oAction[0]);
                saveButtonModel = source.getSubmitModel();
            }
            saveButton.setLabel(castDefault(source.getSubmitLabel(), () -> p.getMessage("n2o.api.action.toolbar.button.submit.label")));
            saveButton.setActions(actions);
            saveButton.setModel(castDefault(saveButtonModel, ReduxModelEnum.RESOLVE));
            saveButton.setValidate(true);
            items[0] = saveButton;

            //create close button
            N2oButton closeButton = new N2oButton();
            closeButton.setId(GenerateTypeEnum.CLOSE.getId());
            closeButton.setLabel(p.getMessage("n2o.api.action.toolbar.button.close.label"));
            N2oCloseAction cancelAction = new N2oCloseAction();
            cancelAction.setId(GenerateTypeEnum.CLOSE.getId());
            closeButton.setModel(ReduxModelEnum.FILTER);
            cancelAction.setRefresh(source.getRefreshOnClose());
            closeButton.setActions(new N2oCloseAction[]{cancelAction});
            closeButton.setValidate(false);
            items[1] = closeButton;
        }
    }

    protected String[] getRefreshDatasourceId(S source, CompileProcessor p) {
        if (source.getRefreshDatasourceIds() != null)
            return source.getRefreshDatasourceIds();

        String datasource = getLocalDatasourceId(p);
        return datasource != null ? new String[]{datasource} : null;
    }
}
