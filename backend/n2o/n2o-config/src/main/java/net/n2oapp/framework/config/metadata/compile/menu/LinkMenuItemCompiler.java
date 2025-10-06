package net.n2oapp.framework.config.metadata.compile.menu;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.menu.N2oLinkMenuItem;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.menu.LinkMenuItem;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initMappings;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initParentRoutePathMappings;

/**
 * Компиляция элемента меню {@code <link>}
 */
@Component
public class LinkMenuItemCompiler extends AbstractMenuItemCompiler<N2oLinkMenuItem, LinkMenuItem> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLinkMenuItem.class;
    }

    @Override
    public LinkMenuItem compile(N2oLinkMenuItem source, CompileContext<?, ?> context, CompileProcessor p) {
        LinkMenuItem compiled = new LinkMenuItem();
        initMenuItem(source, compiled, p);
        compiled.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(property("n2o.api.menu.link.src"), String.class)));
        compiled.setTarget(castDefault(source.getTarget(), TargetEnum.NEW_WINDOW));
        compileHref(compiled, source, p);
        return compiled;
    }

    private void compileHref(LinkMenuItem compiled, N2oLinkMenuItem source, CompileProcessor p) {
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String path = source.getHref();
        if (!StringUtils.isLink(source.getHref())) {
            if (TargetEnum.APPLICATION.equals(compiled.getTarget())) {
                path = RouteUtil.absolute(source.getHref(), routeScope != null ? routeScope.getUrl() : null);
            }
            path = RouteUtil.normalize(path);
        }
        String resolvedPath = p.resolveJS(path);
        compiled.setUrl(resolvedPath);
        if (StringUtils.isJs(resolvedPath)) {
            if (compiled.getDatasource() == null) {
                throw new N2oException("Источник данных не найден для пункта меню \"<link>\" со связанным 'href' " + source.getHref());
            }
        } else {
            compileParams(compiled, source.getPathParams(), source.getQueryParams(), p, routeScope);
        }
    }

    private void compileParams(LinkMenuItem compiled, N2oParam[] pathParams, N2oParam[] queryParams, CompileProcessor p, ParentRouteScope routeScope) {
        ReduxModelEnum model = compiled.getModel();
        if (pathParams != null && model != null) {
            Arrays.stream(pathParams).filter(param -> param.getModel() == null).forEach(param -> param.setModel(model));
        }
        if (queryParams != null && model != null) {
            Arrays.stream(queryParams).filter(param -> param.getModel() == null).forEach(param -> param.setModel(model));
        }
        if (pathParams != null || queryParams != null) {
            Map<String, ModelLink> pathMapping = initParentRoutePathMappings(routeScope, compiled.getUrl());
            Map<String, ModelLink> queryMapping = new LinkedHashMap<>();
            initMappings(pathParams, queryParams, pathMapping, queryMapping, p);
            compiled.setQueryMapping(queryMapping);
            compiled.setPathMapping(pathMapping);
        }
    }
}