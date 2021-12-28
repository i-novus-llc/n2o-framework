package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.Datasource;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.PageRoutesScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.CompileUtil.generateSourceDatasourceId;

/**
 * Базовая компиляция страницы с регионами
 */
public abstract class BasePageCompiler<S extends N2oBasePage, D extends StandardPage> extends PageCompiler<S, D> {

    protected abstract Map<String, List<Region>> initRegions(S source, D page, CompileProcessor p, PageContext context,
                                                             Object... scopes);

    public D compilePage(S source, D page, PageContext context, CompileProcessor p, SourceComponent[] items, SearchBarScope searchBarScope) {
        String pageRoute = initPageRoute(source, context, p);
        page.setId(p.cast(context.getClientPageId(), RouteUtil.convertPathToId(pageRoute)));

        List<N2oWidget> sourceWidgets = collectWidgets(items, p);
        N2oWidget resultWidget = initResultWidget(context, sourceWidgets);

        String pageName = p.cast(context.getPageName(), source.getName());
        page.setPageProperty(initPageName(source, pageName, context, p));
        BreadcrumbList breadcrumb = initBreadcrumb(pageName, context, p);
        page.setBreadcrumb(breadcrumb);

        CompiledObject object = initObject(source, p);
        page.setObject(object);
        page.setClassName(source.getCssClass());
        page.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        compileComponent(page, source, context, p);

        Models models = new Models();
        page.setModels(models);

        PageScope pageScope = initPageScope(source, page, context, sourceWidgets, resultWidget);
        PageRoutes pageRoutes = new PageRoutes();
        pageRoutes.addRoute(new PageRoutes.Route(pageRoute));
        ParentRouteScope routeScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        ValidationList validationList = new ValidationList();
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        SubModelsScope subModelsScope = new SubModelsScope();
        CopiedFieldScope copiedFieldScope = new CopiedFieldScope();
        DataSourcesScope dataSourcesScope = initDataSourcesScope(source);
        MetaActions metaActions = initMetaActions(source);
        FiltersScope filtersScope = new FiltersScope();

        //regions
        IndexScope index = new IndexScope();
        page.setRegions(initRegions(source, page, p, context, pageScope, pageRoutes, routeScope,
                breadcrumb, validationList, models, pageRoutesScope, searchBarScope, subModelsScope,
                copiedFieldScope, dataSourcesScope, metaActions, filtersScope, index));

        //datasources
        Map<String, Datasource> compiledDataSources = compileDataSources(context, p, dataSourcesScope,
                validationList, subModelsScope, copiedFieldScope, pageRoutes, routeScope, pageScope,
                searchBarScope, filtersScope);
        page.setDatasources(compiledDataSources);

        //routes
        registerRoutes(pageRoutes, context, p);
        page.setRoutes(pageRoutes);

        //toolbars
        initToolbarGenerate(source, context, resultWidget);
        compileToolbarAndAction(page, source, context, p,
                pageScope, routeScope, pageRoutes, object, breadcrumb, metaActions, validationList);
        return page;
    }

    private DataSourcesScope initDataSourcesScope(S source) {
        DataSourcesScope dataSourcesScope = new DataSourcesScope();
        if (source.getDatasources() != null)
            Stream.of(source.getDatasources()).forEach(ds -> dataSourcesScope.put(ds.getId(), ds));
        return dataSourcesScope;
    }

    private PageScope initPageScope(S source, D page, PageContext context, List<N2oWidget> sourceWidgets, N2oWidget resultWidget) {
        PageScope pageScope = new PageScope();
        pageScope.setPageId(page.getId());
        if (context.getParentTabIds() != null) {
            pageScope.setTabIds(context.getParentTabIds());
        }
        if (context.getSubmitOperationId() != null || SubmitActionType.copy.equals(context.getSubmitActionType())) {
            pageScope.setObjectId(source.getObjectId());
            pageScope.setResultWidgetId(resultWidget == null ? null : resultWidget.getId());
        }
        if (!CollectionUtils.isEmpty(sourceWidgets))
            pageScope.setWidgetIdQueryIdMap(sourceWidgets.stream().filter(w -> w.getQueryId() != null)
                    .collect(Collectors.toMap(N2oWidget::getId, N2oWidget::getQueryId)));
        pageScope.setWidgetIdClientDatasourceMap(new HashMap<>());
        pageScope.setWidgetIdSourceDatasourceMap(new HashMap<>());
        pageScope.getWidgetIdSourceDatasourceMap().putAll(sourceWidgets.stream()
                .collect(Collectors.toMap(N2oMetadata::getId,
                        w -> w.getDatasourceId() == null ? generateSourceDatasourceId(w.getId()) : w.getDatasourceId())));
        pageScope.getWidgetIdClientDatasourceMap().putAll(sourceWidgets.stream()
                .collect(Collectors.toMap(w -> pageScope.getGlobalWidgetId(w.getId()),
                        w -> pageScope.getGlobalWidgetId(w.getDatasourceId() == null ? generateSourceDatasourceId(w.getId()) : w.getDatasourceId()))));
        if (context.getParentWidgetIdDatasourceMap() != null)
            pageScope.getWidgetIdClientDatasourceMap().putAll(context.getParentWidgetIdDatasourceMap());
        return pageScope;
    }

    private CompiledObject initObject(S source, CompileProcessor p) {
        return source.getObjectId() != null ? p.getCompiled(new ObjectContext(source.getObjectId())) : null;
    }

    private Map<String, Datasource> compileDataSources(PageContext context,
                                                       CompileProcessor p, DataSourcesScope dataSourcesScope, Object... scopes) {
        Map<String, Datasource> compiledDataSources = new HashMap<>();
        for (N2oDatasource ds : dataSourcesScope.values()) {
            Datasource compiled = p.compile(ds, context, scopes);
            compiledDataSources.put(compiled.getId(), compiled);
        }
        return compiledDataSources;
    }

    @Deprecated
    //todo в целевой модели не должно требоваться собирать исходные ввиджеты в список, т.к. виджеты независимы друг от друга
    protected List<N2oWidget> collectWidgets(SourceComponent[] items, CompileProcessor p) {
        List<N2oWidget> result = new ArrayList<>();
        if (items != null) {
            Map<String, Integer> ids = new HashMap<>();
            addWidgets(items, result, ids, "w", p);
        }
        return result;
    }

    private void addWidgets(SourceComponent[] items, List<N2oWidget> result,
                            Map<String, Integer> ids, String prefix, CompileProcessor p) {
        if (!ids.containsKey(prefix))
            ids.put(prefix, 1);
        for (SourceComponent item : items) {
            if (item instanceof N2oWidget) {
                N2oWidget widget = ((N2oWidget) item);
                if (widget.getId() == null)
                    widget.setId(prefix + ids.put(prefix, ids.get(prefix) + 1));
                String refId = ((N2oWidget) item).getRefId();
                if (refId != null && !DynamicUtil.isDynamic(refId))
                    widget = (N2oWidget) p.merge(p.getSource(refId, N2oWidget.class), item);
                result.add(widget);
            } else if (item instanceof N2oTabsRegion) {
                if (((N2oTabsRegion) item).getTabs() != null)
                    for (N2oTabsRegion.Tab tab : ((N2oTabsRegion) item).getTabs())
                        if (tab.getContent() != null)
                            addWidgets(tab.getContent(), result, ids, ((N2oTabsRegion) item).getAlias(), p);
            } else if (item instanceof N2oRegion && ((N2oRegion) item).getContent() != null)
                addWidgets(((N2oRegion) item).getContent(), result, ids, ((N2oRegion) item).getAlias(), p);
        }
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

    private N2oWidget initResultWidget(PageContext context, List<N2oWidget> sourceWidgets) {
        String resultWidgetId = context.getResultWidgetId();
        if (resultWidgetId != null) {
            for (N2oWidget sourceWidget : sourceWidgets) {
                if (resultWidgetId.equals(sourceWidget.getId()))
                    return sourceWidget;
            }
            throw new N2oException("Widget " + resultWidgetId + " not found!");
        } else {
            return !sourceWidgets.isEmpty() ? sourceWidgets.get(0) : null;
        }
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
            if (item.getWidgetId() == null)
                item.setWidgetId(actionsBar.getWidgetId());
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
            n2oToolbar.setDatasource(resultWidget.getDatasourceId());
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