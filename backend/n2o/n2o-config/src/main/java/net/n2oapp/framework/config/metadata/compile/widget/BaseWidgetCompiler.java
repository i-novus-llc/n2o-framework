package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.fieldset.FieldSetScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.CompileUtil;

import java.util.*;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalizeParam;

/**
 * Компиляция абстрактного виджета
 */
public abstract class BaseWidgetCompiler<D extends Widget, S extends N2oWidget> implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    private static final String spread_operator = "*.";

    protected abstract String getPropertyWidgetSrc();

    protected void compileWidget(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                 CompiledObject object) {
        String localWidgetId = initLocalWidgetId(source, p);
        source.setId(localWidgetId);
        compiled.setId(initGlobalWidgetId(source, localWidgetId, context, p));
        compiled.setObjectId(object != null ? object.getId() : null);
        compiled.setName(p.cast(source.getName(), object != null ? object.getName() : null, source.getId()));
        compiled.setRoute(p.cast(source.getRoute(), normalize(source.getId())));
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(property(getPropertyWidgetSrc()), String.class)));
        compiled.setOpened(source.getOpened());
        compiled.setIcon(source.getIcon());
        compiled.setProperties(p.mapAttributes(source));
        compiled.setUpload(p.cast(source.getUpload(), source.getQueryId() != null ? UploadType.query : UploadType.defaults));
        compileFetchDependency(compiled, source, p);
        initFilters(compiled, source, p);
    }

    protected void compileDataProviderAndRoutes(D compiled, S source, CompileProcessor p,
                                                ValidationList validationList) {
        CompiledQuery query = getDataProviderQuery(source, p);
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        String parentRoute = parentRouteScope != null ? parentRouteScope.getUrl() : "";
        String widgetRoute = normalize(parentRoute + p.cast(source.getRoute(), normalize(source.getId())));
        compiled.setDataProvider(initDataProvider(compiled, source, widgetRoute, query, p, validationList, parentRouteScope));
        compileRouteWidget(compiled, source, query, p, widgetRoute);
        compileFetchOnInit(source, compiled);
    }

    protected void collectValidation(FieldSet fs, Map<String, List<Validation>> clientValidations, ValidationScope validationScope) {
        if (fs.getRows() == null)
            return;
        fs.getRows().stream().filter(row -> row.getCols() != null).forEach(row -> {
            row.getCols().stream().forEach(col -> {
                if (col.getFields() != null) {
                    col.getFields().forEach(f -> {
                        if (f.getServerValidations() != null) {
                            validationScope.addAll(f.getServerValidations());
                        }
                        if (f.getClientValidations() != null) {
                            clientValidations.put(f.getId(), f.getClientValidations());
                        }
                    });
                }
                if (col.getFieldsets() != null) {
                    col.getFieldsets().forEach(fieldset -> collectValidation(fieldset, clientValidations, validationScope));
                }
            });
        });
    }

    protected ParentRouteScope initWidgetRoute(String route, CompileContext<?, ?> context, CompileProcessor p) {
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        if (parentRouteScope != null)
            return new ParentRouteScope(route, parentRouteScope);
        if (context instanceof WidgetContext && context.getRoute(p) != null)
            return new ParentRouteScope(context.getRoute(p));
        return new ParentRouteScope(route);
    }

    private String initGlobalWidgetId(S source, String localWidgetId, CompileContext<?, ?> context, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null) {
            return pageScope.getGlobalWidgetId(localWidgetId);
        } else {
            return context.getCompiledId(p);
        }
    }

    private String initLocalWidgetId(S source, CompileProcessor p) {
        if (source.getId() == null) {
            IndexScope indexScope = p.getScope(IndexScope.class);
            return indexScope != null ? "w" + indexScope.get() : "";
        }
        return source.getId();
    }

    protected void compileToolbarAndAction(D compiled, S source, CompileContext<?, ?> context, CompileProcessor p,
                                           WidgetScope widgetScope, ParentRouteScope widgetRoute, MetaActions widgetActions,
                                           CompiledObject object, ValidationList validationList) {
        actionsToToolbar(source);
        compileActions(source, context, p, widgetActions, widgetScope, object, validationList);
        compileToolbar(compiled, source, object, context, p, widgetActions, widgetScope, widgetRoute, validationList);
        compiled.setActions(widgetActions);
    }

    private void compileFetchOnInit(S source, D compiled) {
        if (compiled.getComponent() == null)
            return;
        if (source.getDependsOn() == null && compiled.getDataProvider() != null) {
            compiled.getComponent().setFetchOnInit(true);
        } else {
            compiled.getComponent().setFetchOnInit(false);
        }
    }

    private void actionsToToolbar(S source) {
        if (source.getActions() == null || source.getToolbars() == null)
            return;
        Map<String, ActionsBar> actionMap = new HashMap<>();
        Stream.of(source.getActions()).forEach(a -> actionMap.put(a.getId(), a));
        for (N2oToolbar toolbar : source.getToolbars()) {
            if (toolbar.getItems() == null) continue;
            ToolbarItem[] toolbarItems = toolbar.getItems();
            copyActionForToolbarItem(actionMap, toolbarItems);
        }
    }

    private void copyActionForToolbarItem(Map<String, ActionsBar> actionMap, ToolbarItem[] toolbarItems) {
        for (ToolbarItem item : toolbarItems) {
            if (item instanceof N2oButton || item instanceof N2oMenuItem) {
                copyAction((AbstractMenuItem) item, actionMap);
            } else if (item instanceof N2oSubmenu) {
                for (N2oMenuItem subItem : ((N2oSubmenu) item).getMenuItems()) {
                    copyAction(subItem, actionMap);
                }
            } else if (item instanceof N2oGroup) {
                copyActionForToolbarItem(actionMap, ((N2oGroup) item).getItems());
            }
        }
    }

    private void copyAction(AbstractMenuItem item, Map<String, ActionsBar> actionMap) {
        AbstractMenuItem mi = item;
        if (mi.getAction() == null && mi.getActionId() != null) {
            ActionsBar action = actionMap.get(mi.getActionId());
            if (action == null) {
                throw new N2oException("Toolbar has reference to nonexistent action by actionId {0}!").addData(mi.getActionId());
            }
            mi.setAction(action.getAction());
            if (mi.getModel() == null)
                mi.setModel(action.getModel());
            if (mi.getWidgetId() == null)
                mi.setWidgetId(action.getWidgetId());
            if (mi.getLabel() == null)
                mi.setLabel(action.getLabel());
            if (mi.getIcon() == null)
                mi.setIcon(action.getIcon());
        }
    }

    private void compileToolbar(D compiled, S source, CompiledObject object, CompileContext<?, ?> context, CompileProcessor p
            , MetaActions compiledActions, WidgetScope widgetScope, ParentRouteScope widgetRouteScope, ValidationList validations) {
        if (source.getToolbars() == null)
            return;

        Toolbar compiledToolbar = new Toolbar();
        IndexScope index = new IndexScope();
        for (N2oToolbar toolbar : source.getToolbars()) {
            compiledToolbar.putAll(p.compile(toolbar, context, widgetScope, widgetRouteScope, compiledActions, object, index, validations));
        }
        compiled.setToolbar(compiledToolbar);
    }

    private void compileRouteWidget(D compiled, S source, CompiledQuery query, CompileProcessor p, String widgetRoute) {
        PageRoutes routes = p.getScope(PageRoutes.class);
        if (routes == null)
            return;
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        //Маршрут виджета /page/widget
        routes.addRoute(widgetRoute, compiled.getId());
        //Маршрут с выделенной записью в виджете /page/widget/:widget_id
        //todo для формы не существует selected!
        String selectedId = normalizeParam(compiled.getId() + "_id");
        String routeWidgetSelected = widgetRoute + normalize(colon(selectedId));
        routes.addRoute(routeWidgetSelected, compiled.getId(), true, selectedId);
        ReduxAction widgetIdMapping = Redux.dispatchSelectedWidget(compiled.getId(), colon(selectedId));
        routes.addPathMapping(selectedId, widgetIdMapping);
        compiled.setSelectedRoute(routeWidgetSelected);
        compiled.addPathMapping(selectedId, (ModelLink) Redux.createBindLink(widgetIdMapping));
        if (query != null) {
            ((List<Filter>) compiled.getFilters()).stream().filter(filter -> filter.getReloadable()).forEach(filter -> {
                ReduxAction onGet;
                String filterId = filter.getFilterId();
                if (filterId.contains(spread_operator)) {
                    onGet = Redux.dispatchUpdateMapModel(compiled.getId(), ReduxModel.FILTER,
                            filterId.substring(0, filterId.indexOf(spread_operator)),
                            filterId.substring(filterId.indexOf(spread_operator) + 2), colon(filter.getParam()));
                } else {
                    onGet = Redux.dispatchUpdateModel(compiled.getId(), ReduxModel.FILTER, filterId, colon(filter.getParam()));
                }
                routes.addQueryMapping(filter.getParam(), onGet, filter.getLink());
            });
            for (N2oQuery.Field field : query.getSortingFields()) {
                String sortParam = RouteUtil.normalizeParam("sorting." + source.getId() + "_" + field.getId());
                BindLink onSet = Redux.createBindLink(compiled.getId(), "sorting." + field.getId());
                ReduxAction onGet = Redux.dispatchSortWidget(compiled.getId(), field.getId(), colon(sortParam));
                routes.addQueryMapping(sortParam, onGet, onSet);
            }
        }
    }

    protected CompiledObject getObject(S source, CompileProcessor p) {
        if (source.getObjectId() == null) {
            if (source.getQueryId() != null) {
                CompiledQuery query = p.getCompiled(new QueryContext(source.getQueryId()));
                return query.getObject();
            }
        } else {
            return p.getCompiled(new ObjectContext(source.getObjectId()));
        }
        return null;
    }

    private WidgetDataProvider initDataProvider(D widget, S source, String widgetRoute, CompiledQuery query,
                                                CompileProcessor p, ValidationList validationList, ParentRouteScope parentRouteScope) {
        if (query == null)
            return null;
        WidgetDataProvider dataProvider = new WidgetDataProvider();
        //Адресом URL для провайдера данных виджета будет маршрут виджета на странице
        dataProvider.setUrl(p.resolveText(property("n2o.config.data.route")) + normalize(widgetRoute));
        //Копируем соответствие параметров URL из маршрута страницы в провайдер данных виджета
        Map<String, BindLink> pathMap = new StrictMap<>();
        if (parentRouteScope != null && parentRouteScope.getPathMapping() != null) {
            pathMap.putAll(parentRouteScope.getPathMapping());
        }
        dataProvider.setPathMapping(pathMap);
        if (widget.getFilters() != null) {
            Map<String, BindLink> queryMap = new StrictMap<>();
            ((List<Filter>) widget.getFilters()).stream().filter(f -> !pathMap.containsKey(f.getParam()))
                    .forEach(f -> queryMap.put(f.getParam(), f.getLink()));
            dataProvider.setQueryMapping(queryMap);
        }
        p.addRoute(widgetRoute, getQueryContext(widget, source, widgetRoute, query, validationList));
        return dataProvider;
    }

    protected QueryContext getQueryContext(D widget, S source, String route, CompiledQuery query,
                                           ValidationList validationList) {
        QueryContext queryContext = new QueryContext(query.getId(), route);
        queryContext.setValidations(validationList == null ? null : validationList.get(widget.getId(), ReduxModel.FILTER));
        queryContext.setQuerySize(widget instanceof Form ? 1 : 10);
        queryContext.setFilters(widget.getFilters());
        queryContext.setUpload(widget.getUpload());
        queryContext.setFailAlertWidgetId(getFailAlertWidget(widget));
        queryContext.setSuccessAlertWidgetId(getSuccessAlertWidget(widget));
        queryContext.setMessagesForm(getMessagesForm(widget));

        return queryContext;
    }

    protected String getFailAlertWidget(Widget widget) {
        return widget.getId();
    }

    protected String getSuccessAlertWidget(Widget widget) {
        return widget.getId();
    }

    protected String getMessagesForm(Widget widget) {
        return widget.getId();
    }

    private void compileActions(S source, CompileContext<?, ?> context, CompileProcessor p
            , MetaActions widgetActions, WidgetScope widgetScope, CompiledObject object, ValidationList validationList) {
        if (source.getActions() != null)
            for (ActionsBar a : source.getActions()) {
                a.setModel(p.cast(a.getModel(), ReduxModel.RESOLVE));
                p.compile(a.getAction(), context, widgetScope, widgetActions, object, validationList, new ComponentScope(a));
            }
    }

    private void compileFetchDependency(D compiled, S source, CompileProcessor p) {
        if (source.getDependsOn() != null) {
            FetchDependency dependency = new FetchDependency();
            List<FetchDependency.On> fetch = new ArrayList<>();
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (widgetScope != null) {
                String masterWidgetId = widgetScope.getDependsOnWidgetId();
                ModelLink bindLink = new ModelLink(ReduxModel.RESOLVE, masterWidgetId);
                fetch.add(new FetchDependency.On(bindLink.getBindLink()));
            }
            dependency.setFetch(fetch);
            compiled.setDependency(dependency);
        }
    }

    /**
     * Инициализация выборки виджета, учитывая источник данных
     */
    private CompiledQuery getDataProviderQuery(S source, CompileProcessor p) {
        String queryId = null;
        UploadType upload = source.getUpload();
        if (UploadType.query == upload) {
            queryId = source.getQueryId();
            if (queryId == null)
                throw new N2oException("Upload is 'query', but queryId isn't set in widget");
        } else if (UploadType.defaults == upload) {
            queryId = source.getDefaultValuesQueryId();
        } else if (UploadType.copy == upload) {
            queryId = source.getQueryId();
        } else if (upload == null && source.getQueryId() != null) {
            queryId = source.getQueryId();
        }

        if (queryId != null)
            return p.getCompiled(new QueryContext(queryId));
        return null;
    }

    /**
     * Получить скомпилированную выборку
     */
    protected CompiledQuery getQuery(S source, CompileProcessor p) {
        CompiledQuery query = source.getQueryId() != null ? p.getCompiled(new QueryContext(source.getQueryId())) : null;
        if (query == null) {
            query = source.getDefaultValuesQueryId() != null ? p.getCompiled(new QueryContext(source.getDefaultValuesQueryId())) : null;
        }
        return query;
    }

    protected FieldSetScope initFieldSetScope(CompiledQuery query, CompiledObject object) {
        FieldSetScope scope = new FieldSetScope();
        if (query != null) {
            Map<String, N2oQuery.Field> fieldsMap = query.getFieldsMap();
            for (Map.Entry<String, N2oQuery.Field> entry : fieldsMap.entrySet()) {
                if (entry.getValue() != null) {
                    scope.put(entry.getKey(), entry.getValue().getName());
                }
            }
        }

        if (object != null) {
            Map<String, AbstractParameter> fieldMap = object.getObjectFieldsMap();
            for (Map.Entry<String, AbstractParameter> entry : fieldMap.entrySet()) {
                if (!scope.containsKey(entry.getKey())) {
                    scope.put(entry.getKey(), entry.getValue().getName());
                }
            }
        }
        return scope;
    }

    /**
     * Инициализация филдсетов
     *
     * @param fields      Список полей или филдсетов или строк или столбцов
     * @param context     Контекст сборки
     * @param widgetQuery Выборка виджета
     * @param p           Процессор сборки
     * @return Список филдсетов
     */
    protected List<FieldSet> initFieldSets(NamespaceUriAware[] fields, CompileContext<?, ?> context, CompileProcessor p,
                                           WidgetScope widgetScope,
                                           CompiledQuery widgetQuery,
                                           CompiledObject widgetObject,
                                           ModelsScope modelsScope,
                                           FiltersScope filtersScope) {
        if (fields == null)
            return Collections.emptyList();
        FieldSetScope fieldSetScope = initFieldSetScope(widgetQuery, widgetObject);
        IndexScope indexScope = new IndexScope();
        List<FieldSet> fieldSets = new ArrayList<>();
        int i = 0;
        while (i < fields.length) {
            N2oFieldSet fieldSet;
            if (fields[i] instanceof N2oFieldSet) {
                fieldSet = (N2oFieldSet) fields[i];
                i++;
            } else {
                N2oSetFieldSet newFieldset = new N2oSetFieldSet();
                List<NamespaceUriAware> newFieldsetItems = new ArrayList<>();
                while (i < fields.length && !(fields[i] instanceof N2oFieldSet)) {
                    newFieldsetItems.add(fields[i]);
                    i++;
                }
                NamespaceUriAware[] items = new NamespaceUriAware[newFieldsetItems.size()];
                newFieldset.setItems(newFieldsetItems.toArray(items));
                fieldSet = newFieldset;
            }
            fieldSets.add(p.compile(fieldSet, context, widgetScope, widgetQuery, fieldSetScope, widgetObject, modelsScope, filtersScope, indexScope));
        }
        return fieldSets;
    }

    private void initFilters(D compiled, S source, CompileProcessor p) {
        CompiledQuery query = getDataProviderQuery(source, p);
        if (query == null)
            return;
        List<Filter> filters = new ArrayList<>();
        String masterWidgetId = null;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        PageScope pageScope = p.getScope(PageScope.class);
        if (widgetScope != null) {
            masterWidgetId = widgetScope.getDependsOnWidgetId();
        }
        if (masterWidgetId != null && source.getDetailFieldId() != null) {
            Filter filter = new Filter();
            filter.setFilterId(query.getFilterFieldId(source.getDetailFieldId(), FilterType.eq));
            if (source.getMasterFieldId() == null || source.getMasterFieldId().equals("id")) {
                filter.setParam(normalizeParam(masterWidgetId + "_id"));
            } else {
                filter.setParam(normalizeParam(source.getMasterFieldId()));
            }
            filter.setReloadable(false);
            String masterFieldId = p.cast(source.getMasterFieldId(), "id");
            ModelLink link = new ModelLink(ReduxModel.RESOLVE, masterWidgetId, null, filter.getParam());
            link.setValue(p.resolveJS(Placeholders.ref(masterFieldId)));
            filter.setLink(link);
            filters.add(filter);
        }
        if (source.getPreFilters() != null) {
            for (N2oPreFilter preFilter : source.getPreFilters()) {
                N2oQuery.Filter queryFilter = query.getFilterByPreFilter(preFilter);
                if (queryFilter != null) {
                    Filter filter = new Filter();
                    filter.setParam(p.cast(preFilter.getParam(), compiled.getId() + "_" + queryFilter.getParam()));
                    filter.setReloadable(false);
                    filter.setFilterId(queryFilter.getFilterField());
                    Object prefilterValue = getPrefilterValue(preFilter);
                    if (StringUtils.isJs(prefilterValue)) {
                        String widgetId = masterWidgetId;
                        if (preFilter.getRefWidgetId() != null) {
                            widgetId = preFilter.getRefPageId() == null ?
                                    pageScope.getGlobalWidgetId(preFilter.getRefWidgetId())
                                    : CompileUtil.generateWidgetId(preFilter.getRefPageId(), preFilter.getRefWidgetId());
                        }
                        ReduxModel model = p.cast(preFilter.getRefModel(), ReduxModel.RESOLVE);
                        ModelLink link = new ModelLink(model, widgetId, null, filter.getParam());
                        link.setValue(prefilterValue);
                        filter.setLink(link);
                    } else {
                        ModelLink link = new ModelLink(prefilterValue);
                        filter.setLink(link);
                    }
                    filters.add(filter);
                } else {
                    throw new N2oException("Pre-filter " + preFilter + " not found in query " + query.getId());
                }
            }
        }
        compiled.setFilters(filters);
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
