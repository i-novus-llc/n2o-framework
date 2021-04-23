package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageProperty;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;

import java.util.Map;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Базовая компиляция страницы
 *
 * @param <S> Тип исходной модели страницы
 */
public abstract class PageCompiler<S extends N2oPage, C extends Page> implements BaseSourceCompiler<C, S, PageContext> {

    /**
     * Получение базового маршрута страницы
     *
     * @param source  Исходная страница
     * @param context Контекст сборки
     * @param p       Процессор сборки
     * @return Маршрут
     */
    protected String initPageRoute(N2oPage source, PageContext context, CompileProcessor p) {
        return normalize(p.cast(context.getRoute((N2oCompileProcessor) p), source.getRoute(), normalize(source.getId())));
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
     * @param pageName Наименование страницы
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
        if (context.getParentModelLink() != null) {
            current.setModelLink(context.getParentModelLink());
        }
        breadcrumbs.add(current);
        return breadcrumbs;
    }

    protected PageProperty initPageName(String pageName, boolean showTitle, PageContext context, CompileProcessor p) {
        PageProperty pageProperty = new PageProperty();
        pageProperty.setHtmlTitle(pageName);
        if (context instanceof ModalPageContext) {
            pageProperty.setHeaderTitle(pageName);
        } else if (showTitle)
            pageProperty.setTitle(pageName);
        if (context.getParentModelLink() != null)
            pageProperty.setModelLink(context.getParentModelLink());
        return pageProperty;
    }

    protected abstract String getPropertyPageSrc();
}
