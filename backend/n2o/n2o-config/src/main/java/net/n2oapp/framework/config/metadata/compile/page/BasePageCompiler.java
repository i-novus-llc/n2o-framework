package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.ApplicationDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.datasource.ParentDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientWidgetId;

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

        PageRoutes pageRoutes = new PageRoutes();
        pageRoutes.addRoute(new PageRoutes.Route(pageRoute));
        ParentRouteScope routeScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        ValidationScope validationScope = new ValidationScope();
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        SubModelsScope subModelsScope = new SubModelsScope();
        CopiedFieldScope copiedFieldScope = new CopiedFieldScope();
        ApplicationDatasourceIdsScope appDatasourcesIdScope = new ApplicationDatasourceIdsScope();
        ParentDatasourceIdsScope parentDatasourceIdsScope = new ParentDatasourceIdsScope(context.getParentClientPageId());
        DataSourcesScope datasourcesScope = initDataSourcesScope(source, sourceWidgets, appDatasourcesIdScope, parentDatasourceIdsScope);
        PageScope pageScope = initPageScope(source, page.getId(), sourceWidgets, resultWidget,
                appDatasourcesIdScope, parentDatasourceIdsScope, context, p);
        MetaActions metaActions = initMetaActions(source);
        FiltersScope filtersScope = new FiltersScope();

        //regions
        IndexScope index = new IndexScope();
        page.setRegions(initRegions(source, page, p, context, pageScope, pageRoutes, routeScope,
                breadcrumb, validationScope, page.getModels(), pageRoutesScope, searchBarScope, subModelsScope,
                copiedFieldScope, datasourcesScope, appDatasourcesIdScope, parentDatasourceIdsScope, metaActions, filtersScope, index));

        //datasources
        Map<String, AbstractDatasource> compiledDatasources = compileDatasources(context, p,
                datasourcesScope, pageScope,
                validationScope, subModelsScope, copiedFieldScope, pageRoutes, routeScope,
                searchBarScope, filtersScope, appDatasourcesIdScope, parentDatasourceIdsScope);
        page.setDatasources(compiledDatasources);

        //routes
        registerRoutes(pageRoutes, context, p);
        page.setRoutes(pageRoutes);

        //toolbars
        initToolbarGenerate(source, context, resultWidget);
        compileToolbarAndAction(page, source, context, p, pageScope, routeScope, pageRoutes, object,
                breadcrumb, metaActions, validationScope, datasourcesScope, appDatasourcesIdScope, parentDatasourceIdsScope);

        if (source.getDatasourceId() != null)
            page.getPageProperty().setDatasource(getClientDatasourceId(source.getDatasourceId(), page.getId(), p));

        return page;
    }

    private DataSourcesScope initDataSourcesScope(S source, List<N2oWidget> sourceWidgets,
                                                  ApplicationDatasourceIdsScope appDatasourcesIdScope,
                                                  ParentDatasourceIdsScope parentDatasourceIdsScope) {
        DataSourcesScope datasourcesScope = new DataSourcesScope();
        if (source.getDatasources() != null)
            Stream.of(source.getDatasources()).forEach(ds -> {
                if (ds instanceof N2oApplicationDatasource)
                    appDatasourcesIdScope.add(ds.getId());
                else if (ds instanceof N2oParentDatasource) {
                    if (parentDatasourceIdsScope.getDatasources() == null)
                        parentDatasourceIdsScope.setDatasources(new HashSet<>());
                    parentDatasourceIdsScope.getDatasources().add(ds.getId());
                }
                datasourcesScope.put(ds.getId(), ds);
            });

        if (parentDatasourceIdsScope.getDatasources() != null && parentDatasourceIdsScope.getPageId() == null)
            throw new N2oException("На странице задан `<parent-datasource>`, при этом она не имеет родительской страницы");

        addInlineDatasourcesToScope(sourceWidgets, datasourcesScope);
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
                                    ParentDatasourceIdsScope parentDatasourceIds,
                                    PageContext context, CompileProcessor p) {
        PageScope pageScope = new PageScope();
        pageScope.setPageId(pageId);
        if (context.getParentTabIds() != null) {
            pageScope.setTabIds(context.getParentTabIds());
        }
        pageScope.setObjectId(source.getObjectId());
        pageScope.setResultWidgetId(resultWidget == null ? null : resultWidget.getId());
        if (!CollectionUtils.isEmpty(sourceWidgets))
            pageScope.setWidgetIdQueryIdMap(sourceWidgets.stream().filter(w -> w.getQueryId() != null)
                    .collect(Collectors.toMap(N2oWidget::getId, N2oWidget::getQueryId)));
        pageScope.getWidgetIdSourceDatasourceMap().putAll(sourceWidgets.stream()
                .collect(Collectors.toMap(N2oMetadata::getId,
                        w -> w.getDatasourceId() == null ? w.getId() : w.getDatasourceId())));
        pageScope.setWidgetIdClientDatasourceMap(new HashMap<>());
        pageScope.getWidgetIdClientDatasourceMap().putAll(sourceWidgets.stream()
                .collect(Collectors.toMap(w -> getClientWidgetId(w.getId(), pageId),
                        w -> {
                            String datasourceId = w.getDatasourceId() == null ? w.getId() : w.getDatasourceId();
                            if (applicationDatasourceIds.contains(datasourceId))
                                return datasourceId;
                            else if (parentDatasourceIds.getDatasources() != null &&
                                    parentDatasourceIds.getDatasources().contains(datasourceId) && parentDatasourceIds.getPageId() != null)
                                return getClientDatasourceId(datasourceId, parentDatasourceIds.getPageId(), p);
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
        initContextDatasources(dataSourcesScope, pageScope, context, p);
        for (N2oAbstractDatasource ds : dataSourcesScope.values())
            if (!(ds instanceof N2oApplicationDatasource)) {
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
        String place = p.cast(n2oRegion.getPlace(), defaultPlace);
        if (regionMap.get(place) != null) {
            regionMap.get(place).add(region);
        } else {
            List<Region> regionList = new ArrayList<>();
            regionList.add(region);
            regionMap.put(place, regionList);
        }
    }

    private N2oWidget initResultWidget(List<N2oWidget> sourceWidgets) {
        return !sourceWidgets.isEmpty() ? sourceWidgets.get(0) : null;
    }

    private void compileToolbarAndAction(StandardPage compiled, S source, PageContext context, CompileProcessor p,
                                         Object... scopes) {
        actionsToToolbar(source);
        compiled.setToolbar(compileToolbar(source, context, p, scopes));
    }

    private MetaActions initMetaActions(S source) {
        MetaActions metaActions = new MetaActions();
        if (source.getActions() != null) {
            for (ActionsBar actionsBar : source.getActions()) {
                metaActions.addAction(actionsBar.getId(), actionsBar);
            }
        }
        return metaActions;
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
            if (item instanceof N2oButton) {
                copyAction((N2oButton) item, actionMap);
            } else if (item instanceof N2oSubmenu) {
                for (N2oButton subItem : ((N2oSubmenu) item).getMenuItems()) {
                    copyAction(subItem, actionMap);
                }
            } else if (item instanceof N2oGroup) {
                copyActionForToolbarItem(actionMap, ((N2oGroup) item).getItems());
            }
        }
    }

    private void copyAction(N2oButton item, Map<String, ActionsBar> actionMap) {
        if (item.getAction() == null && item.getActionId() != null) {
            ActionsBar actionsBar = actionMap.get(item.getActionId());
            if (actionsBar == null) {
                throw new N2oException(String.format("Toolbar has reference to nonexistent action by actionId %s!", item.getActionId()));
            }
            item.setAction(actionsBar.getAction());
            if (item.getModel() == null)
                item.setModel(actionsBar.getModel());
            if (item.getDatasourceId() == null)
                item.setDatasourceId(actionsBar.getDatasourceId());
            if (item.getLabel() == null)
                item.setLabel(actionsBar.getLabel());
            if (item.getIcon() == null)
                item.setIcon(actionsBar.getIcon());
        }
    }

    private Toolbar compileToolbar(S source, PageContext context, CompileProcessor p,
                                   Object... scopes) {
        if (source.getToolbars() == null)
            return null;
        ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.page.toolbar.place"), String.class));
        Toolbar toolbar = new Toolbar();
        for (N2oToolbar n2oToolbar : source.getToolbars()) {
            toolbar.putAll(p.compile(n2oToolbar, context, new IndexScope(), toolbarPlaceScope, scopes));
        }
        return toolbar;
    }

    private void initToolbarGenerate(S source, PageContext context, N2oWidget resultWidget) {
        if (context.getSubmitOperationId() != null || SubmitActionType.copy.equals(context.getSubmitActionType())) {
            N2oToolbar n2oToolbar = new N2oToolbar();
            String[] generate = new String[]{GenerateType.submit.name(), GenerateType.close.name()};
            n2oToolbar.setGenerate(generate);
            n2oToolbar.setTargetWidgetId(resultWidget != null ? resultWidget.getId() : null);
            n2oToolbar.setDatasourceId(resultWidget.getDatasourceId());
            if (source.getToolbars() == null) {
                source.setToolbars(new N2oToolbar[0]);
            }
            int length = source.getToolbars().length;
            N2oToolbar[] n2oToolbars = new N2oToolbar[length + 1];
            System.arraycopy(source.getToolbars(), 0, n2oToolbars, 0, length);
            n2oToolbars[length] = n2oToolbar;
            source.setToolbars(n2oToolbars);
        }
    }
}