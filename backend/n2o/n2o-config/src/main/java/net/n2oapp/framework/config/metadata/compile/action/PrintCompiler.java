package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oPrint;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.print.Print;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;


/**
 * Компиляция действия печати
 */
@Component
public class PrintCompiler extends AbstractActionCompiler<Print, N2oPrint> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPrint.class;
    }

    @Override
    public Print compile(N2oPrint source, CompileContext<?, ?> context, CompileProcessor p) {
        Print print = new Print();
        source.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.link.src"), String.class)));
        compileAction(print, source, p);
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

    private void initMappings(Print compiled, N2oPrint source, CompileProcessor p, ParentRouteScope routeScope) {
        Map<String, ModelLink> pathMapping = new StrictMap<>();
        Map<String, ModelLink> queryMapping = new StrictMap<>();
        if (routeScope != null && routeScope.getPathMapping() != null) {
            List<String> pathParams = RouteUtil.getParams(compiled.getPayload().getUrl());
            routeScope.getPathMapping().forEach((k, v) -> {
                if (pathParams.contains(k)) {
                    pathMapping.put(k, v);
                }
            });
        }

        WidgetScope scope = p.getScope(WidgetScope.class);
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (scope != null) {
            String defaultClientWidgetId = getDefaultClientWidgetId(p, scope, componentScope);
            ReduxModel defaultModel = getTargetWidgetModel(p, ReduxModel.RESOLVE);
            if (source.getPathParams() != null) {
                initMapping(p, pathMapping, defaultClientWidgetId, defaultModel, source.getPathParams());
            }
            if (source.getQueryParams() != null) {
                initMapping(p, queryMapping, defaultClientWidgetId, defaultModel, source.getQueryParams());
            }
        }
        compiled.getPayload().setQueryMapping(queryMapping);
        compiled.getPayload().setPathMapping(pathMapping);
    }

    private void initMapping(CompileProcessor p, Map<String, ModelLink> mapping, String defaultClientWidgetId,
                             ReduxModel defaultModel, N2oParam[] params) {
        for (N2oParam pathParam : params) {
            ModelLink link = new ModelLink(p.cast(pathParam.getRefModel(), defaultModel),
                    p.cast(pathParam.getRefWidgetId(), defaultClientWidgetId));
            link.setValue(p.resolveJS(pathParam.getValue()));
            mapping.put(pathParam.getName(), link);
        }
    }

    private String getDefaultClientWidgetId(CompileProcessor p, WidgetScope scope, ComponentScope componentScope) {
        String defaultClientWidgetId = scope.getClientWidgetId();
        if (componentScope != null) {
            WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
            if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                PageScope pageScope = p.getScope(PageScope.class);
                defaultClientWidgetId = pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());
            }
        }
        return defaultClientWidgetId;
    }

}
