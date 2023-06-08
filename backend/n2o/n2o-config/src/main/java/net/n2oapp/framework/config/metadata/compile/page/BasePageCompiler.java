package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.event.Event;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.ApplicationDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.datasource.ParentDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientWidgetId;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Базовая компиляция страницы с регионами
 */
public abstract class BasePageCompiler<S extends N2oBasePage, D extends StandardPage> extends PageCompiler<S, D> {

    protected abstract Map<String, List<Region>> initRegions(S source, D page, CompileProcessor p, PageContext context,
                                                             Object... scopes);

    public D compilePage(S source, D page, PageContext context, CompileProcessor p, SearchBarScope searchBarScope) {
        String pageName = p.cast(context.getPageName(), source.getName());
        page.setPageProperty(initPageName(source, pageName, context, p));
        compileBaseProperties(source, page, context, p);
        String pageRoute = initPageRoute(source, context, p);

        List<N2oWidget> sourceWidgets = collectWidgets(source, p);
        N2oWidget resultWidget = initResultWidget(sourceWidgets);

        BreadcrumbList breadcrumb = initBreadcrumb(source, pageName, context, p);
        page.setBreadcrumb(breadcrumb);

        CompiledObject object = initObject(source, p);
        page.setClassName(source.getCssClass());
        page.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compileComponent(page, source, context, p);

        PageIndexScope pageIndexScope = new PageIndexScope(page.getId());
        PageRoutes pageRoutes = new PageRoutes();
        pageRoutes.addRoute(new PageRoutes.Route(pageRoute));
        ParentRouteScope routeScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        ValidationScope validationScope = new ValidationScope();
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        SubModelsScope subModelsScope = new SubModelsScope();
        CopiedFieldScope copiedFieldScope = new CopiedFieldScope();

        ApplicationDatasourceIdsScope appDatasourceIdsScope = new ApplicationDatasourceIdsScope();
        ParentDatasourceIdsScope parentDatasourceIdsScope = new ParentDatasourceIdsScope();
        DataSourcesScope datasourcesScope = initDataSourcesScope(source, sourceWidgets, appDatasourceIdsScope,
                parentDatasourceIdsScope, context, p);
        PageScope pageScope = initPageScope(source, page.getId(), sourceWidgets, resultWidget,
                appDatasourceIdsScope, context, p);
        initContextDatasources(datasourcesScope, appDatasourceIdsScope, parentDatasourceIdsScope, pageScope, context, p);

        //actions
        mergeActions(source, context);
        MetaActions metaActions = initMetaActions(source, p);
        if (source.getActions() != null)
            context.setActions(Arrays.stream(source.getActions())
                    .collect(Collectors.toMap(ActionBar::getId, Function.identity())));

        FiltersScope filtersScope = new FiltersScope();

        //regions
        IndexScope index = new IndexScope();
        page.setRegions(initRegions(source, page, p, context, pageScope, pageRoutes, routeScope,
                breadcrumb, validationScope, page.getModels(), pageRoutesScope, searchBarScope, subModelsScope,
                copiedFieldScope, datasourcesScope, appDatasourceIdsScope, parentDatasourceIdsScope, metaActions,
                filtersScope, index, pageIndexScope));

        //datasources
        Map<String, AbstractDatasource> compiledDatasources = compileDatasources(context, p,
                datasourcesScope, pageScope,
                validationScope, subModelsScope, copiedFieldScope, pageRoutes, routeScope,
                searchBarScope, filtersScope, appDatasourceIdsScope, parentDatasourceIdsScope);
        page.setDatasources(compiledDatasources);

        //routes
        registerRoutes(pageRoutes, context, p);
        page.setRoutes(pageRoutes);

        //toolbars
        mergeToolbars(source, context, resultWidget, p);
        compileToolbarAndAction(page, source, context, p, metaActions, pageIndexScope, pageScope, routeScope, pageRoutes,
                object, breadcrumb, metaActions, validationScope, datasourcesScope, appDatasourceIdsScope,
                parentDatasourceIdsScope);

        //events
        page.setEvents(initEvents(source, context, p, metaActions, pageScope, pageIndexScope,
                datasourcesScope, appDatasourceIdsScope, parentDatasourceIdsScope));

        if (source.getDatasourceId() != null)
            page.getPageProperty().setDatasource(getClientDatasourceId(source.getDatasourceId(), page.getId(), p));

        return page;
    }

    private List<Event> initEvents(S source, PageContext context, CompileProcessor p, Object... scopes) {
        if (isNotEmpty(source.getEvents()))
            return Arrays.stream(source.getEvents())
                    .map(n2oEvent -> ((Event) p.compile(n2oEvent, context, scopes)))
                    .collect(Collectors.toList());
        return null;
    }

    private DataSourcesScope initDataSourcesScope(S source, List<N2oWidget> sourceWidgets,
                                                  ApplicationDatasourceIdsScope appDatasourceIdsScope,
                                                  ParentDatasourceIdsScope parentDatasourceIdsScope,
                                                  PageContext context, CompileProcessor p) {
        DataSourcesScope datasourcesScope = new DataSourcesScope();
        Set<String> parentDatasourceIds = new HashSet<>();

        if (source.getDatasources() != null)
            for (N2oAbstractDatasource ds : source.getDatasources()) {
                if (ds instanceof N2oApplicationDatasource)
                    appDatasourceIdsScope.put(ds.getId(), p.cast(((N2oApplicationDatasource) ds).getSourceDatasource(), ds.getId()));
                if (ds instanceof N2oParentDatasource) {
                    parentDatasourceIds.add(ds.getId());
                    if (context.getParentDatasourceIdsMap() != null && !context.getParentDatasourceIdsMap().containsKey(ds.getId())) {
                        throw new N2oException("Элемент \"<parent-datasource>\" ссылается на несуществующий источник родительской страницы");
                    }
                }
                datasourcesScope.put(ds.getId(), ds);
            }

        if (!parentDatasourceIds.isEmpty() && context.getParentClientPageId() == null)
            throw new N2oException("На странице задан \"<parent-datasource>\", при этом она не имеет родительской страницы");

        addInlineDatasourcesToScope(sourceWidgets, datasourcesScope);

        // add datasources from parent page as N2oParentDatasource
        if (context.getParentDatasourceIdsMap() != null)
            for (String dsId : context.getParentDatasourceIdsMap().keySet()) {
                if (datasourcesScope.containsKey(dsId) && !parentDatasourceIds.contains(dsId))
                    continue;

                if (!datasourcesScope.containsKey(dsId))
                    datasourcesScope.put(dsId, new N2oParentDatasource(dsId, context.getParentDatasourceIdsMap().get(dsId), true));
                parentDatasourceIdsScope.put(dsId, context.getParentDatasourceIdsMap().get(dsId));
            }

        return datasourcesScope;
    }

    @Deprecated
    private void addInlineDatasourcesToScope(List<N2oWidget> sourceWidgets, DataSourcesScope dataSourcesScope) {
        for (N2oWidget widget : sourceWidgets) {
            if (widget.getDatasourceId() == null && (widget.getRefId() == null || !DynamicUtil.isDynamic(widget.getRefId()))) {
                N2oStandardDatasource datasource;
                String datasourceId = widget.getId();
                if (widget.getDatasource() == null) {
                    datasource = new N2oStandardDatasource();
                    datasource.setDefaultValuesMode(DefaultValuesMode.defaults);
                } else {
                    datasource = widget.getDatasource();
                    widget.setDatasource(null);
                }
                datasource.setId(datasourceId);
                widget.setDatasourceId(datasourceId);
                dataSourcesScope.put(datasourceId, datasource);
            }
        }
    }

    private PageScope initPageScope(S source, String pageId, List<N2oWidget> sourceWidgets, N2oWidget resultWidget,
                                    ApplicationDatasourceIdsScope applicationDatasourceIds,
                                    PageContext context, CompileProcessor p) {
        PageScope pageScope = new PageScope();
        pageScope.setPageId(pageId);
        if (context.getParentTabIds() != null)
            pageScope.setTabIds(context.getParentTabIds());
        pageScope.setObjectId(source.getObjectId());
        pageScope.setResultWidgetId(resultWidget == null ? null : resultWidget.getId());
        pageScope.getWidgetIdSourceDatasourceMap().putAll(sourceWidgets.stream()
                .collect(Collectors.toMap(N2oMetadata::getId,
                        w -> w.getDatasourceId() == null ? w.getId() : w.getDatasourceId())));
        pageScope.setWidgetIdClientDatasourceMap(new HashMap<>());
        pageScope.getWidgetIdClientDatasourceMap().putAll(sourceWidgets.stream()
                .collect(Collectors.toMap(w -> getClientWidgetId(w.getId(), pageId),
                        w -> {
                            String datasourceId = w.getDatasourceId() == null ? w.getId() : w.getDatasourceId();
                            if (applicationDatasourceIds.containsKey(datasourceId))
                                return applicationDatasourceIds.get(datasourceId);
                            else
                                return getClientDatasourceId(datasourceId, pageId, p);
                        })));
        if (context.getParentWidgetIdDatasourceMap() != null)
            pageScope.getWidgetIdClientDatasourceMap().putAll(context.getParentWidgetIdDatasourceMap());
        return pageScope;
    }

    private CompiledObject initObject(S source, CompileProcessor p) {
        return source.getObjectId() != null ? p.getCompiled(new ObjectContext(source.getObjectId())) : null;
    }

    private Map<String, AbstractDatasource> compileDatasources(PageContext context,
                                                               CompileProcessor p,
                                                               DataSourcesScope dataSourcesScope,
                                                               PageScope pageScope,
                                                               Object... scopes) {
        Map<String, AbstractDatasource> compiledDataSources = new HashMap<>();
        for (N2oAbstractDatasource ds : dataSourcesScope.values())
            if (!(ds instanceof N2oApplicationDatasource || ds instanceof N2oParentDatasource)) {
                AbstractDatasource compiled = p.compile(ds, context, pageScope, dataSourcesScope, scopes);
                compiledDataSources.put(compiled.getId(), compiled);
            }
        return compiledDataSources;
    }

    protected List<N2oWidget> collectWidgets(S source, CompileProcessor p) {
        List<N2oWidget> widgets = source.getWidgets();
        return mergeNotDynamic(widgets, p);
    }

    private List<N2oWidget> mergeNotDynamic(List<N2oWidget> widgets, CompileProcessor p) {
        List<N2oWidget> result = new ArrayList<>();
        for (N2oWidget w : widgets) {
            String refId = w.getRefId();
            if (refId != null && !DynamicUtil.isDynamic(refId))
                w = p.merge(p.getSource(refId, N2oWidget.class), w);
            result.add(w);
        }
        return result;
    }

    /**
     * Инициализация всех регионов страницы и добавление их в контейнер со всеми регионами страницы
     *
     * @param pageItems    Элементы страницы (регионы/виджеты)
     * @param regionMap    Контейнер для хранения всех регионов страницы по их позициям на странице.
     * @param defaultPlace Позиция регионов по умолчанию
     * @param context      Контекст страницы
     * @param p            Процессор сборки метаданных
     * @param scopes       Массив scope c информацией для сборки
     */
    protected void initRegions(SourceComponent[] pageItems, Map<String, List<Region>> regionMap, String defaultPlace,
                               PageContext context, CompileProcessor p, Object... scopes) {
        if (pageItems == null) return;

        List<N2oWidget> widgets = new ArrayList<>();
        BasePageUtil.resolveRegionItems(pageItems,
                item -> {
                    if (!widgets.isEmpty())
                        createCustomRegion(widgets, regionMap, defaultPlace, context, p, scopes);
                    createRegion(item, regionMap, defaultPlace, context, p, scopes);
                },
                widgets::add);
        if (!widgets.isEmpty())
            createCustomRegion(widgets, regionMap, defaultPlace, context, p, scopes);
    }

    /**
     * Компиляция простого региона, который будет оборачивать один или несколько подряд идущих виджетов
     * вне регионов, и добавление его в контейнер со всеми регионами страницы
     *
     * @param widgets      Список виджетов
     * @param regionMap    Контейнер для хранения всех регионов страницы по их позициям на странице.
     * @param defaultPlace Позиция региона по умолчанию
     * @param context      Контекст страницы
     * @param p            Процессор сборки метаданных
     * @param scopes       Массив scope c информацией для сборки
     */
    private void createCustomRegion(List<N2oWidget> widgets, Map<String, List<Region>> regionMap, String defaultPlace,
                                    PageContext context, CompileProcessor p, Object... scopes) {
        N2oRegion n2oRegion = new N2oCustomRegion();
        N2oWidget[] content = new N2oWidget[widgets.size()];
        widgets.toArray(content);
        n2oRegion.setContent(content);
        createRegion(n2oRegion, regionMap, defaultPlace, context, p, scopes);
        widgets.clear();
    }

    /**
     * Компиляция региона и добавление его в контейнер со всеми регионами страницы
     *
     * @param n2oRegion    Исходная модель региона
     * @param regionMap    Контейнер для хранения всех регионов страницы по их позициям на странице.
     *                     Скомпилированный регион будет добавлен в контейнер
     * @param defaultPlace Позиция региона по умолчанию
     * @param context      Контекст страницы
     * @param p            Процессор сборки метаданных
     * @param scopes       Массив scope c информацией для сборки
     */
    private void createRegion(N2oRegion n2oRegion, Map<String, List<Region>> regionMap, String defaultPlace,
                              PageContext context, CompileProcessor p, Object... scopes) {
        Region region = p.compile(n2oRegion, context, scopes);
        if (regionMap.get(defaultPlace) != null) {
            regionMap.get(defaultPlace).add(region);
        } else {
            List<Region> regionList = new ArrayList<>();
            regionList.add(region);
            regionMap.put(defaultPlace, regionList);
        }
    }

    private N2oWidget initResultWidget(List<N2oWidget> sourceWidgets) {
        return !sourceWidgets.isEmpty() ? sourceWidgets.get(0) : null;
    }

    private void compileToolbarAndAction(StandardPage compiled, S source, PageContext context, CompileProcessor p,
                                         MetaActions metaActions, PageIndexScope pageIndexScope, Object... scopes) {
        actionsToToolbar(source, metaActions);
        compileMetaActions(source, context, p, pageIndexScope, metaActions, scopes);
        compiled.setToolbar(compileToolbar(source, "n2o.api.page.toolbar.place", context, p, metaActions,
                metaActions, metaActions, new ComponentScope(source), pageIndexScope, scopes));
    }


    private void mergeActions(S source, PageContext context) {
        if (CollectionUtils.isEmpty(context.getActions())) return;
        Map<String, ActionBar> actionBars = new HashMap<>(context.getActions());
        if (source.getActions() != null) {
            Arrays.stream(source.getActions()).forEach(a -> actionBars.putIfAbsent(a.getId(), a));
        }
        source.setActions(actionBars.values().toArray(new ActionBar[0]));
    }

    private void mergeToolbars(S source, PageContext context, N2oWidget resultWidget, CompileProcessor p) {
        if (CollectionUtils.isEmpty(context.getToolbars())) return;
        Map<String, N2oToolbar> toolbars = new HashMap<>();
        /* клонируем тулбары из контекста в тулбары страницы, это необходимо, чтобы просатвить значения по умолчанию
         для datasource и при это не испортить контекст изменениями */
        context.getToolbars().forEach(t -> {
            N2oToolbar toolbar = cloneToolbar(t, resultWidget, p);
            toolbars.put(t.getPlace(), toolbar);
        });
        if (source.getToolbars() != null) {
            Arrays.stream(source.getToolbars()).forEach(t -> toolbars.putIfAbsent(t.getPlace(), t));
        }
        source.setToolbars(toolbars.values().toArray(new N2oToolbar[0]));
    }
}