package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBreadcrumb;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oAbstractButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.Models;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageProperty;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.datasource.ClientDatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.datasource.DataSourcesScope;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.List;

import static java.util.Objects.requireNonNullElseGet;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Базовая компиляция страницы
 *
 * @param <S> Тип исходной модели страницы
 */
public abstract class PageCompiler<S extends N2oPage, C extends Page> extends ComponentCompiler<C, S, PageContext> implements BaseSourceCompiler<C, S, PageContext> {

    /**
     * Компиляция базовых свойств страницы
     *
     * @param source  Исходная модель страницы
     * @param page    Клиентская модель страницы
     * @param context Контекст сборки
     * @param p       Процессор сборки
     */
    protected void compileBaseProperties(S source, C page, PageContext context, CompileProcessor p) {
        page.setId(castDefault(context.getClientPageId(), () -> RouteUtil.convertPathToId(initPageRoute(source, context, p))));
        Models models = new Models();
        page.setModels(models);
        page.getPageProperty().setModel(castDefault(source.getModel(), ReduxModel.resolve));
    }

    /**
     * Получение базового маршрута страницы
     *
     * @param source  Исходная страница
     * @param context Контекст сборки
     * @param p       Процессор сборки
     * @return Маршрут
     */
    protected String initPageRoute(N2oPage source, PageContext context, CompileProcessor p) {
        return normalize(castDefault(context.getRoute((N2oCompileProcessor) p), source.getRoute(), normalize(source.getId())));
    }

    /**
     * Регистрация маршрутов страницы
     *
     * @param routes  Маршруты страницы
     * @param context Контекст сборки
     * @param p       Процессор сборки
     */
    protected void registerRoutes(PageRoutes routes, PageContext context, CompileProcessor p) {
        for (PageRoutes.Route route : routes.getList()) {
            if (!route.getIsOtherPage())
                p.addRoute(route.getPath(), context);
        }
    }

    /**
     * Получение базового маршрута страницы
     *
     * @param source   Исходная модель страницы
     * @param pageName Наименование страницы
     * @param context  Контекст сборки
     * @param p        Процессор сборки метаданных
     * @return breadcrumb текущей страницы
     */
    protected BreadcrumbList initBreadcrumb(N2oPage source, String pageName, PageContext context, CompileProcessor p) {
        if (Boolean.TRUE.equals(context.getBreadcrumbFromParent())) {
            BreadcrumbList breadcrumbList = new BreadcrumbList();
            for (Breadcrumb breadcrumb : context.getBreadcrumbs()) {
                breadcrumb.setPath(resolvePath(breadcrumb.getPath(), context));
                breadcrumb.setModelLinks(context.getParentModelLinks());
                breadcrumbList.add(breadcrumb);
            }
            return breadcrumbList;
        }

        boolean needCreation = source.getHasBreadcrumbs() || p.resolve(property("n2o.api.page.breadcrumbs"), Boolean.class);
        if (needCreation) {
            if (source.getBreadcrumbs() == null)
                return initBreadcrumbByContext(pageName, context);

            BreadcrumbList breadcrumbs = new BreadcrumbList();
            for (N2oBreadcrumb sourceCrumb : source.getBreadcrumbs()) {
                breadcrumbs.add(new Breadcrumb(
                        p.resolveJS(sourceCrumb.getLabel()), resolvePath(sourceCrumb.getPath(), context)));
            }
            return breadcrumbs;
        }
        return null;
    }

    private String resolvePath(String path, PageContext context) {
        if (!RouteUtil.hasRelativity(path))
            return path;

        Integer nestingLevel = RouteUtil.getRelativeLevel(path);
        N2oException noRouteException = new N2oException("Родительский маршрут не найден для пути '" + path + "'");
        List<String> parentRoutes = requireNonNullElseGet(context.getParentRoutes(), () -> {
            throw noRouteException;
        });
        if (nestingLevel > parentRoutes.size())
            throw noRouteException;

        return requireNonNullElseGet(parentRoutes.get(parentRoutes.size() - nestingLevel), () -> {
            throw noRouteException;
        });
    }

    /**
     * Получение базового маршрута страницы по контексту сборки
     *
     * @param pageName Наименование страницы
     * @param context  Контекст сборки
     * @return breadcrumb текущей страницы
     */
    protected BreadcrumbList initBreadcrumbByContext(String pageName, PageContext context) {
        if (context instanceof ModalPageContext)
            return null;
        BreadcrumbList breadcrumbs = new BreadcrumbList();
        if (context.getBreadcrumbs() != null && !context.getBreadcrumbs().isEmpty()) {
            for (Breadcrumb breadcrumb : context.getBreadcrumbs()) {
                breadcrumbs.add(new Breadcrumb(breadcrumb));
            }
            Breadcrumb prev = breadcrumbs.get(breadcrumbs.size() - 1);
            prev.setPath(castDefault(prev.getPath(), context.getParentRoute(), "/"));
        }
        Breadcrumb current = new Breadcrumb();
        current.setLabel(pageName);
        if (context.getParentModelLinks() != null) {
            current.setModelLinks(context.getParentModelLinks());
        }
        breadcrumbs.add(current);
        return breadcrumbs;
    }

    /**
     * Инициализация заголовков страницы
     *
     * @param source   Исходная модель страницы
     * @param pageName Наименование страницы
     * @param context  Контекст страницы
     * @param p        Процессор сборки метаданных
     * @return Модель с инициализированными заголовками страницы
     */
    protected PageProperty initPageName(N2oPage source, String pageName, PageContext context, CompileProcessor p) {
        PageProperty pageProperty = new PageProperty();
        boolean showTitle = castDefault(source.getShowTitle(), () -> p.resolve(property("n2o.api.page.show_title"), Boolean.class));

        pageProperty.setHtmlTitle(castDefault(source.getHtmlTitle(), pageName));
        if (context instanceof ModalPageContext)
            pageProperty.setModalHeaderTitle(pageName);
        else if (showTitle)
            pageProperty.setTitle(castDefault(source.getTitle(), pageName));

        if (context.getParentModelLinks() != null)
            pageProperty.setModelLinks(context.getParentModelLinks());
        return pageProperty;
    }

    protected void initContextDatasources(DataSourcesScope dataSourcesScope, ClientDatasourceIdsScope clientDatasourceIdsScope,
                                          PageScope pageScope, PageContext context, CompileProcessor p) {
        if (context.getDatasources() != null) {
            for (N2oAbstractDatasource ctxDs : context.getDatasources()) {
                String dsId = ctxDs.getId() != null ? ctxDs.getId() : pageScope.getResultWidgetId();
                if (dataSourcesScope.containsKey(dsId) && ctxDs instanceof N2oStandardDatasource)
                    dataSourcesScope.put(dsId, p.merge(dataSourcesScope.get(dsId), ctxDs));
                else {
                    ctxDs.setId(dsId);
                    if (ctxDs instanceof N2oApplicationDatasource) {
                        clientDatasourceIdsScope.put(ctxDs.getId(), castDefault(((N2oApplicationDatasource) ctxDs).getSourceDatasource(), ctxDs.getId()));
                    } else if (ctxDs instanceof N2oParentDatasource) {
                        String sourceDatasourceId = castDefault(((N2oParentDatasource) ctxDs).getSourceDatasource(), ctxDs.getId());
                        if (context.getParentDatasourceIdsMap().containsKey(sourceDatasourceId))
                            clientDatasourceIdsScope.put(dsId, context.getParentDatasourceIdsMap().get(sourceDatasourceId));
                    }
                    dataSourcesScope.put(dsId, ctxDs);
                }
            }
        }
    }

    /**
     * Клонирование тулбара для проставления значений по умолчанию
     * @return
     */
    protected N2oToolbar cloneToolbar(N2oToolbar t, N2oWidget resultWidget, CompileProcessor p) {
        N2oToolbar toolbar = new N2oToolbar();
        toolbar.setPlace(castDefault(t.getPlace(), () -> p.resolve(property("n2o.api.page.toolbar.place"), String.class)));
        toolbar.setGenerate(t.getGenerate());
        toolbar.setStyle(t.getStyle());
        toolbar.setDatasourceId(t.getDatasourceId());
        toolbar.setCssClass(t.getCssClass());
        if (t.getItems() != null) {
            ToolbarItem[] items = new ToolbarItem[t.getItems().length];
            for (int i = 0; i < t.getItems().length; i++) {
                items[i] = CompileUtil.copy(t.getItems()[i]);
                if (N2oAbstractButton.class.isAssignableFrom(items[i].getClass())) {
                    ((N2oAbstractButton) items[i])
                            .setDatasourceId(((N2oAbstractButton) items[i]).getDatasourceId() == null ?
                                    resultWidget.getDatasourceId() : ((N2oAbstractButton) items[i]).getDatasourceId());
                }
            }
            toolbar.setItems(items);
        }
        return toolbar;
    }
}
