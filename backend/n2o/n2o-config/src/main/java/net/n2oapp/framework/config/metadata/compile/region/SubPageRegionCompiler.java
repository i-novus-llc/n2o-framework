package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.region.N2oSubPageRegion;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.BreadcrumbList;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.region.SubPageRegion;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.SubPageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.util.PageContextCompileUtil.*;

/**
 * Компиляция региона `<sub-page>`
 */
@Component
public class SubPageRegionCompiler implements BaseSourceCompiler<SubPageRegion, N2oSubPageRegion, CompileContext<?, ?>> {

    private static final Pattern SUBROUTE_ANT_PATTERN = Pattern.compile(":\\w+");

    @Override
    public SubPageRegion compile(N2oSubPageRegion source, CompileContext<?, ?> context, CompileProcessor p) {
        SubPageRegion compiled = new SubPageRegion();
        compiled.setSrc(p.resolve(property("n2o.api.region.sub_page.src"), String.class));
        compilePages(source, compiled, context, p);
        compiled.setDefaultPageId(compileDefaultPageId(source.getDefaultPageId(), source.getPages(), compiled.getPages()));
        return compiled;
    }

    private void compilePages(N2oSubPageRegion source, SubPageRegion compiled,
                              CompileContext<?, ?> context, CompileProcessor p) {
        if (ArrayUtils.isEmpty(source.getPages()))
            return;

        List<SubPageRegion.Page> pages = new ArrayList<>();
        ParentRouteScope parentRouteScope = p.getScope(ParentRouteScope.class);
        String parentRoute = normalize(castDefault(parentRouteScope != null ? parentRouteScope.getUrl() : null,
                () -> context.getRoute((N2oCompileProcessor) p),
                () -> ""));

        List<String> subRoutes = new ArrayList<>();
        int subpageIndex = 1;
        for (N2oSubPageRegion.Page sourcePage : source.getPages()) {
            SubPageRegion.Page page = new SubPageRegion.Page();
            String route = normalize(sourcePage.getRoute());
            page.setRoute(route.startsWith(".") ? route : "." + route);
            page.setUrl(parentRoute + "/subpage" + subpageIndex++);
            page.setId(RouteUtil.convertPathToId(page.getUrl()));
            subRoutes.add(SUBROUTE_ANT_PATTERN.matcher(parentRoute + page.getRoute().substring(1))
                    .replaceAll("*"));
            initPageContext(sourcePage, page, parentRoute, context, p);
            pages.add(page);
        }
        compiled.setPages(pages);
        ((PageContext) context).setSubRoutes(subRoutes);

        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        if (pageRoutes != null) {
            pageRoutes.setSubRoutes(
                    pages.stream().map(SubPageRegion.Page::getRoute).toList()
            );
            pageRoutes.setPath(parentRoute);
        }
    }

    private String compileDefaultPageId(String defaultPageId,
                                        N2oSubPageRegion.Page[] sourcePages,
                                        List<SubPageRegion.Page> pages) {
        if (CollectionUtils.isEmpty(pages))
            return null;
        if (defaultPageId == null)
            return pages.get(0).getId();

        int idx = -1;
        for (int i = 0; i < sourcePages.length; i++) {
            if (defaultPageId.equals(sourcePages[i].getPageId())) {
                idx = i;
                break;
            }
        }
        return pages.get(idx).getId();
    }

    private void initPageContext(N2oSubPageRegion.Page sourcePage, SubPageRegion.Page page,
                                 String parentRoute, CompileContext<?, ?> context, CompileProcessor p) {
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);

        PageContext pageContext = new SubPageContext(sourcePage.getPageId(), page.getUrl());

        Map<String, ModelLink> pathMapping = new HashMap<>();
        Map<String, ModelLink> queryMapping = new LinkedHashMap<>();
        if (routeScope != null) {
            pathMapping.putAll(routeScope.getPathMapping());
            queryMapping.putAll(routeScope.getQueryMapping());
        }
        pageContext.setPathRouteMapping(pathMapping);
        pageContext.setQueryRouteMapping(queryMapping);

        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope != null && pageScope.getTabIds() != null)
            pageContext.setParentTabIds(pageScope.getTabIds());

        pageContext.setBreadcrumbs(initBreadcrumb(sourcePage, pageContext, p));
        if (sourcePage.getDatasources() != null) {
            if (pageContext.getDatasources() == null)
                pageContext.setDatasources(new ArrayList<>());
            pageContext.getDatasources().addAll(Arrays.asList(sourcePage.getDatasources()));
        }
        if (sourcePage.getToolbars() != null)
            pageContext.setToolbars(new ArrayList<>(List.of(sourcePage.getToolbars())));

        if (sourcePage.getActions() != null)
            pageContext.setActions(Arrays.stream(sourcePage.getActions())
                    .collect(Collectors.toMap(ActionBar::getId, Function.identity())));

        pageContext.setParentClientPageId(pageScope == null ? null : pageScope.getPageId());
        pageContext.setParentRoute(RouteUtil.addQueryParams(parentRoute, queryMapping));
        if (context instanceof PageContext ctx) {
            pageContext.addParentRoute(pageContext.getParentRoute(), ctx);
            pageContext.setParentDatasourceIdsMap(initParentDatasourceIdsMap(ctx, p));
        }

        p.addRoute(pageContext);
    }

    private List<Breadcrumb> initBreadcrumb(N2oSubPageRegion.Page sourcePage, PageContext pageContext, CompileProcessor p) {
        if (sourcePage.getBreadcrumbs() != null) {
            pageContext.setBreadcrumbFromParent(true);
            return Arrays.stream(sourcePage.getBreadcrumbs())
                    .map(crumb -> new Breadcrumb(crumb.getLabel(), crumb.getPath()))
                    .toList();
        }

        pageContext.setBreadcrumbFromParent(false);
        return p.getScope(BreadcrumbList.class);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSubPageRegion.class;
    }
}
