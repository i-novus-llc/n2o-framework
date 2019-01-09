package net.n2oapp.framework.config.metadata.compile.page;


import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.meta.*;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Базовая компиляция страницы
 *
 * @param <S> Тип исходной модели страницы
 */
public abstract class BasePageCompiler<S extends N2oPage> implements BaseSourceCompiler<Page, S, PageContext> {


    /**
     * Получение базового маршрута страницы
     *
     * @param source  Исходная страница
     * @param context Контекст сборки
     * @param p       Процессор сборки
     * @return Маршрут
     */
    protected String initPageRoute(N2oPage source, PageContext context, CompileProcessor p) {
        return p.cast(context.getRoute(p), source.getRoute(), normalize(source.getId()));
    }

    /**
     * Регистрация маршрутов страницы
     *
     * @param routes  Маршруты страницы
     * @param context Контекст сборки
     * @param p       Процессор сборки
     */
    protected void registerRoutes(PageRoutes routes, PageContext context, CompileProcessor p) {
        PageContext pageContext = new PageContext(context, p);
        for (PageRoutes.Route route : routes.getList()) {
            if (!route.getIsOtherPage())
                p.addRoute(route.getPath(), pageContext);
        }
    }

    /**
     * Получение базового маршрута страницы
     *
     * @param pageName Наименование страницы
     * @param context  Контекст сборки
     * @param context  Контекст сборки
     * @return breadcrumb текущей страницы
     */
    protected BreadcrumbList initBreadcrumb(String pageName, PageContext context, CompileProcessor p) {
        if (context instanceof ModalPageContext)
            return null;
        BreadcrumbList breadcrumbs = new BreadcrumbList();
        if (context.getBreadcrumbs() != null && !context.getBreadcrumbs().isEmpty()) {
            for (Breadcrumb breadcrumb : context.getBreadcrumbs()) {
                breadcrumbs.add(new Breadcrumb(breadcrumb));
            }
            Breadcrumb prev = breadcrumbs.get(breadcrumbs.size() - 1);
            prev.setPath(p.cast(prev.getPath(), context.getParentRoute(), "/"));
        }
        Breadcrumb current = new Breadcrumb();
        current.setLabel(pageName);
        breadcrumbs.add(current);
        return breadcrumbs;
    }

    protected PageProperty initPageName(String pageName, PageContext context, CompileProcessor p) {
        PageProperty pageProperty = new PageProperty();
        pageProperty.setTitle(p.cast(context.getPageName(), pageName));
        if (context.getParentModel() != null && context.getParentWidgetId() != null)
            pageProperty.setModelLink(new ModelLink(context.getParentModel(), context.getParentWidgetId()));
        return pageProperty;
    }

}
