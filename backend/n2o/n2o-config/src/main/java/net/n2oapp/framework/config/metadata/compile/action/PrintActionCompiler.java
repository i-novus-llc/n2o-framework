package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oPrintAction;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.print.PrintAction;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;


/**
 * Компиляция действия печати
 */
@Component
public class PrintActionCompiler extends AbstractActionCompiler<PrintAction, N2oPrintAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPrintAction.class;
    }

    @Override
    public PrintAction compile(N2oPrintAction source, CompileContext<?, ?> context, CompileProcessor p) {
        PrintAction print = new PrintAction();
        source.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.link.src"), String.class)));
        compileAction(print, source, p);
        print.setType(p.resolve(property("n2o.api.action.print.type"), String.class));
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String path = RouteUtil.absolute(source.getUrl(), routeScope != null ? routeScope.getUrl() : null);
        print.getPayload().setUrl(path);

        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        if (pageRoutes != null) {
            PageRoutes.Route pageRoute = new PageRoutes.Route(path);
            pageRoute.setIsOtherPage(true);
            pageRoutes.addRoute(pageRoute);
        }

        initMappings(print, source, p, routeScope);
        return print;
    }

    private void initMappings(PrintAction compiled, N2oPrintAction source, CompileProcessor p, ParentRouteScope routeScope) {
        Map<String, ModelLink> pathMapping = initParentRoutePathMappings(routeScope, compiled.getPayload().getUrl());
        Map<String, ModelLink> queryMapping = new StrictMap<>();
        initMappings(source.getPathParams(), source.getQueryParams(), pathMapping, queryMapping, p);

        compiled.getPayload().setQueryMapping(queryMapping);
        compiled.getPayload().setPathMapping(pathMapping);
    }
}
