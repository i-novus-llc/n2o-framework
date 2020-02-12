package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionsBar;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.metadata.compile.*;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetObjectScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Базовая компиляция страницы с регионами
 */
public abstract class BasePageCompiler<S extends N2oBasePage> extends PageCompiler<S, StandardPage> {

    protected abstract void initRegions(S source, StandardPage page, CompileProcessor p, PageContext context, PageScope pageScope);

    public StandardPage compilePage(S source, PageContext context, CompileProcessor p, N2oRegion[] regions) {
        StandardPage page = new StandardPage();
        List<N2oWidget> sourceWidgets = collectWidgets(regions);
        String pageRoute = initPageRoute(source, context, p);
        page.setId(p.cast(context.getClientPageId(), RouteUtil.convertPathToId(pageRoute)));
        PageScope pageScope = new PageScope();
        pageScope.setPageId(page.getId());
        String resultWidgetId = null;
        if (context.getSubmitOperationId() != null) {
            pageScope.setObjectId(source.getObjectId());
            resultWidgetId = initResultWidgetId(context, sourceWidgets);
            pageScope.setResultWidgetId(resultWidgetId);
        }
        String pageName = p.cast(context.getPageName(), source.getName());
        page.setPageProperty(initPageName(pageName, context, p));
        page.setProperties(p.mapAttributes(source));
        BreadcrumbList breadcrumb = initBreadcrumb(pageName, context, p);
        page.setBreadcrumb(breadcrumb);
        page.setWidgets(new StrictMap<>());
        Models models = new Models();
        page.setModels(models);
        //init base route
        PageRoutes pageRoutes = new PageRoutes();
        pageRoutes.addRoute(new PageRoutes.Route(pageRoute));
        initDefaults(context, sourceWidgets);
        ParentRouteScope routeScope = new ParentRouteScope(pageRoute, context.getPathRouteMapping(), context.getQueryRouteMapping());
        ValidationList validationList = new ValidationList(new HashMap<>());
        PageRoutesScope pageRoutesScope = new PageRoutesScope();
        //compile widget
        WidgetObjectScope widgetObjectScope = new WidgetObjectScope();
        page.setWidgets(initWidgets(routeScope, pageRoutes, sourceWidgets,
                context, p, pageScope, breadcrumb, validationList, models, pageRoutesScope, widgetObjectScope));
        registerRoutes(pageRoutes, context, p);
        if (!(context instanceof ModalPageContext))
            page.setRoutes(pageRoutes);
        //compile region
        initRegions(source, page, p, context, pageScope);
        CompiledObject object = source.getObjectId() != null ? p.getCompiled(new ObjectContext(source.getObjectId())) : null;
        page.setObject(object);
        page.setSrc(p.cast(source.getSrc(), p.resolve(property(getPropertyPageSrc()), String.class)));
        page.setProperties(p.mapAttributes(source));
        if (context.getSubmitOperationId() != null)
            initToolbarGenerate(source, resultWidgetId);
        MetaActions metaActions = new MetaActions();
        compileToolbarAndAction(page, source, context, p, metaActions, pageScope, routeScope, pageRoutes, object, breadcrumb, validationList, page.getWidgets(), widgetObjectScope);
        page.setActions(metaActions);
        return page;
    }

    protected List<N2oWidget> collectWidgets(N2oRegion[] regions) {
        List<N2oWidget> result = new ArrayList<>();
        Map<String, Integer> ids = new HashMap<>();
        if (regions != null) {
            for (N2oRegion region : regions) {
                if (!ids.containsKey(region.getAlias())) {
                    ids.put(region.getAlias(), 1);
                }
                if (region.getWidgets() != null) {
                    result.addAll(Arrays.stream(region.getWidgets()).map(w -> {
                        if (w.getId() == null) {
                            String widgetPrefix = region.getAlias();
                            w.setId(widgetPrefix + ids.put(widgetPrefix, ids.get(widgetPrefix) + 1));
                        }
                        return w;
                    }).collect(Collectors.toList()));
                }
            }
        }
        return result;
    }

    private void initDefaults(PageContext context, List<N2oWidget> sourceWidgets) {
        if ((context.getPreFilters() != null && !context.getPreFilters().isEmpty()) || (context.getUpload() != null)) {
            N2oWidget widget = initResultWidget(context, sourceWidgets);
            widget.addPreFilters(context.getPreFilters());
            widget.setUpload(context.getUpload());
        }
    }

    private String initResultWidgetId(PageContext context, List<N2oWidget> sourceWidgets) {
        String resultWidgetId = context.getResultWidgetId();
        if (resultWidgetId == null) {
            List<N2oWidget> sourceIndependents = getSourceIndependents(sourceWidgets);
            if (sourceIndependents.size() == 1)
                resultWidgetId = sourceIndependents.get(0).getId();//todo может быть не задан id в виджете
            else
                throw new N2oException("Can't get result widget id. There were two independent's widgets");
        }
        return resultWidgetId;
    }

    private N2oWidget initResultWidget(PageContext context, List<N2oWidget> sourceWidgets) {
        String resultWidgetId = context.getResultWidgetId();
        if (resultWidgetId != null) {
            for (N2oWidget sourceWidget : sourceWidgets) {
                if (sourceWidget.getId() != null && sourceWidget.getId().equals(resultWidgetId))
                    return sourceWidget;
            }
            throw new N2oException("Widget " + resultWidgetId + " not found!");
        } else {
            List<N2oWidget> sourceIndependents = getSourceIndependents(sourceWidgets);
            if (sourceIndependents.size() == 1)
                return sourceIndependents.get(0);//todo может быть не задан id в виджете
            else
                throw new N2oException("Can't get result widget id. There were two independent's widgets");
        }
    }

    private void compileToolbarAndAction(StandardPage compiled, S source, PageContext context, CompileProcessor p,
                                         MetaActions metaActions, PageScope pageScope, ParentRouteScope routeScope, PageRoutes pageRoutes,
                                         CompiledObject object, BreadcrumbList breadcrumbs, ValidationList validationList, Map<String, Widget> widgets, WidgetObjectScope widgetObjectScope) {
        actionsToToolbar(source);
        compiled.setToolbar(compileToolbar(source, context, p, metaActions, pageScope, routeScope, pageRoutes, object, breadcrumbs, validationList, widgetObjectScope));
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
        if (item.getAction() == null && item.getActionId() != null) {
            ActionsBar actionsBar = actionMap.get(item.getActionId());
            if (actionsBar == null) {
                throw new N2oException("Toolbar has reference to nonexistent action by actionId {0}!").addData(item.getActionId());
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
                                   MetaActions metaActions, PageScope pageScope, ParentRouteScope routeScope, PageRoutes pageRoutes,
                                   CompiledObject object, BreadcrumbList breadcrumbs, ValidationList validationList, WidgetObjectScope widgetObjectScope) {
        if (source.getToolbars() == null)
            return null;
        Toolbar toolbar = new Toolbar();
        for (N2oToolbar n2oToolbar : source.getToolbars()) {
            toolbar.putAll(p.compile(n2oToolbar, context, metaActions, pageScope, routeScope, pageRoutes, object, breadcrumbs, validationList, new IndexScope(), widgetObjectScope));
        }
        return toolbar;
    }

    private Map<String, Widget> initWidgets(ParentRouteScope routeScope, PageRoutes pageRoutes, List<N2oWidget> sourceWidgets,
                                            PageContext context, CompileProcessor p,
                                            PageScope pageScope, BreadcrumbList breadcrumbs, ValidationList validationList,
                                            Models models, PageRoutesScope pageRoutesScope, WidgetObjectScope widgetObjectScope) {
        Map<String, Widget> compiledWidgets = new StrictMap<>();
        IndexScope indexScope = new IndexScope();
        List<N2oWidget> independents = getSourceIndependents(sourceWidgets);
        independents.forEach(w -> compileWidget(w, pageRoutes, routeScope, null, null,
                sourceWidgets, compiledWidgets,
                context, p,
                pageScope, breadcrumbs, validationList, models, indexScope, pageRoutesScope, widgetObjectScope));
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
                               PageScope pageScope, BreadcrumbList breadcrumbs, ValidationList validationList,
                               Models models, IndexScope indexScope,
                               PageRoutesScope pageRoutesScope, WidgetObjectScope widgetObjectScope) {
        WidgetScope widgetScope = new WidgetScope();
        widgetScope.setDependsOnWidgetId(parentWidgetId);
        widgetScope.setDependsOnQueryId(parentQueryId);
        Widget compiledWidget = p.compile(w, context, indexScope, routes, pageScope, widgetScope, parentRoute,
                breadcrumbs, validationList, models, pageRoutesScope, widgetObjectScope);
        compiledWidgets.put(compiledWidget.getId(), compiledWidget);
        //compile detail widgets
        ParentRouteScope parentRouteScope = new ParentRouteScope(compiledWidget.getRoute(), parentRoute);
        getDetails(w.getId(), sourceWidgets).forEach(detWgt ->
                compileWidget(detWgt, routes, parentRouteScope, compiledWidget.getId(), compiledWidget.getQueryId(),
                        sourceWidgets, compiledWidgets,
                        context, p,
                        pageScope, breadcrumbs, validationList, models, indexScope, pageRoutesScope, widgetObjectScope));
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