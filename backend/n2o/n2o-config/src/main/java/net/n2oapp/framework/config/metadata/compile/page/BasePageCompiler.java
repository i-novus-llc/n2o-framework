package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.SubmitActionType;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oCustomRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarPlaceScope;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.CompileUtil;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Базовая компиляция страницы с регионами
 */
public abstract class BasePageCompiler<S extends N2oBasePage, D extends StandardPage> extends PageCompiler<S, D> {

    protected abstract void initRegions(S source, D page, CompileProcessor p, PageContext context,
                                        PageScope pageScope, PageRoutes pageRoutes, PageWidgetsScope pageWidgetsScope);

    public D compilePage(S source, D page, PageContext context, CompileProcessor p, SourceComponent[] items, SearchBarScope searchBarScope) {
        List<N2oWidget> sourceWidgets = collectWidgets(items, p);
        String pageRoute = initPageRoute(source, context, p);
        page.setId(p.cast(context.getClientPageId(), RouteUtil.convertPathToId(pageRoute)));
        PageScope pageScope = new PageScope();
        pageScope.setPageId(page.getId());
        N2oWidget resultWidget = initResultWidget(context, sourceWidgets);
        if (context.getSubmitOperationId() != null || SubmitActionType.copy.equals(context.getSubmitActionType())) {
            pageScope.setObjectId(source.getObjectId());
            pageScope.setResultWidgetId(resultWidget == null ? null : resultWidget.getId());
        }
        String pageName = p.cast(context.getPageName(), source.getName());
        page.setPageProperty(initPageName(source, pageName, context, p));
        page.setProperties(p.mapAttributes(source));
        BreadcrumbList breadcrumb = initBreadcrumb(pageName, context, p);
        page.setBreadcrumb(breadcrumb);
        page.setClassName(source.getCssClass());
        page.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        Models models = new Models();
        page.setModels(models);
        //init base route
        PageRoutes pageRoutes = new PageRoutes();
        pageRoutes.addRoute(new PageRoutes.Route(pageRoute));
        ParentRouteScope routeScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        ValidationList validationList = new ValidationList(new HashMap<>());
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        //compile widget
        WidgetObjectScope widgetObjectScope = new WidgetObjectScope();
        if (!CollectionUtils.isEmpty(sourceWidgets))
            pageScope.setWidgetIdQueryIdMap(sourceWidgets.stream().filter(w -> w.getQueryId() != null)
                    .collect(Collectors.toMap(N2oWidget::getId, N2oWidget::getQueryId)));
        pageScope.setWidgetIdDatasourceMap(new HashMap<>());
        pageScope.getWidgetIdDatasourceMap().putAll(sourceWidgets.stream()
                .collect(Collectors.toMap(w -> pageScope.getGlobalWidgetId(w.getId()),
                        w -> pageScope.getGlobalWidgetId(w.getDatasource() == null ? w.getId() : w.getDatasource()))));
        if (context.getParentWidgetIdDatasourceMap() != null)
            pageScope.getWidgetIdDatasourceMap().putAll(context.getParentWidgetIdDatasourceMap());
        Map<String, Widget> compiledWidgets = initWidgets(routeScope, pageRoutes, sourceWidgets, context, p, pageScope,
                breadcrumb, validationList, models, pageRoutesScope, widgetObjectScope, searchBarScope);
        registerRoutes(pageRoutes, context, p);
        page.setRoutes(pageRoutes);
        //compile region
        initRegions(source, page, p, context, pageScope, pageRoutes, new PageWidgetsScope(compiledWidgets));
        CompiledObject object = source.getObjectId() != null ? p.getCompiled(new ObjectContext(source.getObjectId())) : null;
        page.setObject(object);
        compileComponent(page, source, context, p);
        page.setProperties(p.mapAttributes(source));
        if (context.getSubmitOperationId() != null || SubmitActionType.copy.equals(context.getSubmitActionType()))
            initToolbarGenerate(source, resultWidget == null ? null : resultWidget.getId());
        MetaActions metaActions = new MetaActions();
        compileToolbarAndAction(page, source, context, p, metaActions, pageScope, routeScope, pageRoutes, object, breadcrumb,
                validationList, compiledWidgets, widgetObjectScope);
        page.setActions(metaActions);
        return page;
    }

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

    private void initDefaults(PageContext context, N2oWidget n2oWidget) {
        if (n2oWidget != null && ((context.getPreFilters() != null && !context.getPreFilters().isEmpty()) || (context.getUpload() != null))) {
            n2oWidget.addPreFilters(context.getPreFilters());
            n2oWidget.setUpload(context.getUpload());
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
            List<N2oWidget> sourceIndependents = getSourceIndependents(sourceWidgets);
            if (!sourceIndependents.isEmpty())
                return sourceIndependents.get(0);
            else
                return null;
        }
    }

    private void compileToolbarAndAction(StandardPage compiled, S source, PageContext context, CompileProcessor p,
                                         MetaActions metaActions, PageScope pageScope, ParentRouteScope routeScope,
                                         PageRoutes pageRoutes, CompiledObject object, BreadcrumbList breadcrumbs,
                                         ValidationList validationList, Map<String, Widget> widgets,
                                         WidgetObjectScope widgetObjectScope) {
        actionsToToolbar(source);
        compiled.setToolbar(compileToolbar(source, context, p, metaActions, pageRoutes, object, breadcrumbs, validationList,
                pageScope, routeScope, widgetObjectScope));
        compileActions(source, context, p, metaActions, pageScope, routeScope, pageRoutes, breadcrumbs, object, validationList, widgets);
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
                                   MetaActions metaActions, PageRoutes pageRoutes, CompiledObject object,
                                   BreadcrumbList breadcrumbs, ValidationList validationList, Object... scopes) {
        if (source.getToolbars() == null)
            return null;
        ToolbarPlaceScope toolbarPlaceScope = new ToolbarPlaceScope(p.resolve(property("n2o.api.page.toolbar.place"), String.class));
        Toolbar toolbar = new Toolbar();
        for (N2oToolbar n2oToolbar : source.getToolbars()) {
            toolbar.putAll(p.compile(n2oToolbar, context, metaActions, pageRoutes, object,
                    breadcrumbs, validationList, new IndexScope(), toolbarPlaceScope, scopes));
        }
        return toolbar;
    }

    private Map<String, Widget> initWidgets(ParentRouteScope routeScope, PageRoutes pageRoutes, List<N2oWidget> sourceWidgets,
                                            PageContext context, CompileProcessor p, PageScope pageScope,
                                            BreadcrumbList breadcrumbs, ValidationList validationList,
                                            Models models, PageRoutesScope pageRoutesScope,
                                            WidgetObjectScope widgetObjectScope, SearchBarScope searchBarScope) {
        Map<String, Widget> compiledWidgets = new StrictMap<>();
        IndexScope indexScope = new IndexScope();
        List<N2oWidget> independents = getSourceIndependents(sourceWidgets);
        if (searchBarScope != null && searchBarScope.getWidgetId() == null) {
            searchBarScope.setWidgetId(independents.get(0).getId());
        }
        independents.forEach(w -> {
            initDefaults(context, w);
            compileWidget(w, pageRoutes, routeScope, null, null, sourceWidgets,
                    compiledWidgets, context, p, breadcrumbs, validationList, models, indexScope,
                    searchBarScope, pageScope, pageRoutesScope, widgetObjectScope);});
        return compiledWidgets;
    }

    private void compileWidget(N2oWidget w,
                               PageRoutes routes,
                               ParentRouteScope parentRoute,
                               String parentWidgetId,
                               String parentQueryId,
                               List<N2oWidget> sourceWidgets,
                               Map<String, Widget> compiledWidgets,
                               PageContext context, CompileProcessor p,
                               BreadcrumbList breadcrumbs, ValidationList validationList,
                               Models models, IndexScope indexScope,
                               SearchBarScope searchBarScope, Object... scopes) {
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setDependsOnWidgetId(parentWidgetId);
        widgetScope.setDependsOnQueryId(parentQueryId);
        Widget compiledWidget = p.compile(w, context, indexScope, routes, widgetScope, parentRoute,
                breadcrumbs, validationList, models, searchBarScope, scopes);
        compiledWidgets.put(compiledWidget.getId(), compiledWidget);
        //compile detail widgets
        ParentRouteScope parentRouteScope = new ParentRouteScope(compiledWidget.getRoute(), parentRoute);
        getDetails(w.getId(), sourceWidgets).forEach(detWgt ->
                compileWidget(detWgt, routes, parentRouteScope, compiledWidget.getId(),
                        compiledWidget.getQueryId(), sourceWidgets, compiledWidgets, context, p,
                        breadcrumbs, validationList, models, indexScope, searchBarScope, scopes));
    }

    private void initToolbarGenerate(S source, String resultWidgetId) {
        N2oToolbar n2oToolbar = new N2oToolbar();
        String[] generate = new String[]{GenerateType.submit.name(), GenerateType.close.name()};
        n2oToolbar.setGenerate(generate);
        n2oToolbar.setTargetWidgetId(resultWidgetId);
        if (source.getToolbars() == null) {
            source.setToolbars(new N2oToolbar[0]);
        }
        int length = source.getToolbars().length;
        N2oToolbar[] n2oToolbars = new N2oToolbar[length + 1];
        System.arraycopy(source.getToolbars(), 0, n2oToolbars, 0, length);
        n2oToolbars[length] = n2oToolbar;
        source.setToolbars(n2oToolbars);
    }

    private List<N2oWidget> getSourceIndependents(List<N2oWidget> sourceWidgets) {
        List<N2oWidget> independents = new ArrayList<>();
        for (N2oWidget widget : sourceWidgets) {
            if (widget.getDependsOn() == null)
                independents.add(widget);
        }
        return independents;
    }

    private List<N2oWidget> getDetails(String widgetId, List<N2oWidget> widgets) {
        List<N2oWidget> details = new ArrayList<>();
        for (N2oWidget widget : widgets) {
            if (widget.getDependsOn() != null && widget.getDependsOn().equals(widgetId))
                details.add(widget);
        }
        return details;
    }

    private void compileActions(S source, PageContext context, CompileProcessor p,
                                MetaActions actions, PageScope pageScope, ParentRouteScope routeScope, PageRoutes pageRoutes,
                                BreadcrumbList breadcrumbs, CompiledObject object, ValidationList validationList, Map<String, Widget> widgets) {
        if (source.getActions() != null) {
            Stream.of(source.getActions()).forEach(a -> {
                a.getAction().setId(a.getId());
                Widget widget = null;
                CompiledObject compiledObject = object;
                if (widgets.containsKey(a.getWidgetId()))
                    widget = widgets.get(a.getWidgetId());
                if (widget != null) {
                    compiledObject = new CompiledObject();
                    compiledObject.setId(widget.getObjectId());
                }
                Action action = p.compile(a.getAction(), context, pageScope, routeScope, pageRoutes, compiledObject, breadcrumbs, validationList, new ComponentScope(a));
                actions.addAction(a.getId(), action);
            });
        }
    }
}