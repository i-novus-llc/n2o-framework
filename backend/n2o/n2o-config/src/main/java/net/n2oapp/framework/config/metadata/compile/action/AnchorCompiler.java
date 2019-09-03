package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionOptions;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ссылки
 */
@Component
public class AnchorCompiler extends AbstractActionCompiler<LinkAction, N2oAnchor> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAnchor.class;
    }

    @Override
    public LinkAction compile(N2oAnchor source, CompileContext<?, ?> context, CompileProcessor p) {
        LinkAction linkAction = new LinkAction(new LinkActionOptions());
        source.setSrc(p.cast(source.getSrc(), p.resolve(property("n2o.api.action.link.src"), String.class)));
        compileAction(linkAction, source, p);
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        String path = RouteUtil.absolute(source.getHref(), routeScope != null ? routeScope.getUrl() : null);
        linkAction.getOptions().setPath(path);
        Target target = p.cast(source.getTarget(), RouteUtil.isApplicationUrl(source.getHref()) ? Target.application : Target.self);
        linkAction.getOptions().setTarget(target);
        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        if (pageRoutes != null && Target.application.equals(source.getTarget())) {
            PageRoutes.Route pageRoute = new PageRoutes.Route(path);
            pageRoute.setIsOtherPage(true);
            pageRoutes.addRoute(pageRoute);
        }
        initPathMapping(linkAction, source, p, routeScope);
        return linkAction;
    }

    private void initPathMapping(LinkAction compiled, N2oAnchor source, CompileProcessor p, ParentRouteScope routeScope ) {
        Map<String, ModelLink> pathMapping = new StrictMap<>();
        if (routeScope != null && routeScope.getPathMapping() != null) {
            List<String> pathParams = RouteUtil.getParams(compiled.getOptions().getPath());
            routeScope.getPathMapping().forEach((k, v) -> {
                if (pathParams.contains(k)) {
                    pathMapping.put(k, v);
                }
            });
        }

        WidgetScope scope = p.getScope(WidgetScope.class);
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (scope != null) {
            String clientWidgetId = scope.getClientWidgetId();
            if (componentScope != null) {
                WidgetIdAware widgetIdAware = componentScope.unwrap(WidgetIdAware.class);
                if (widgetIdAware != null && widgetIdAware.getWidgetId() != null) {
                    PageScope pageScope = p.getScope(PageScope.class);
                    clientWidgetId = pageScope.getGlobalWidgetId(widgetIdAware.getWidgetId());
                }
            }
            if (clientWidgetId != null && componentScope != null &&
                    componentScope.unwrap(ModelAware.class) != null) {
                ReduxModel model = componentScope.unwrap(ModelAware.class).getModel();
                if (source.getPathParams() != null) {
                    for (N2oAnchor.Param pathParam : source.getPathParams()) {
                        ModelLink link = new ModelLink(p.cast(model, ReduxModel.RESOLVE), clientWidgetId);
                        link.setValue(p.resolveJS(pathParam.getValue()));
                        pathMapping.put(pathParam.getName(), link);
                    }

                }
                if (source.getQueryParams() != null) {
                    Map<String, ModelLink> queryMapping = new StrictMap<>();
                    for (N2oAnchor.Param pathParam : source.getQueryParams()) {
                        ModelLink link = new ModelLink(p.cast(model, ReduxModel.RESOLVE), clientWidgetId);
                        link.setValue(p.resolveJS(pathParam.getValue()));
                        queryMapping.put(pathParam.getName(), link);
                    }
                    compiled.getOptions().setQueryMapping(queryMapping);
                }
            }
        }
        compiled.getOptions().setPathMapping(pathMapping);
    }

    private String getRef (String value) {
        if (value != null && value.startsWith("{") && value.endsWith("}")) {
            return value.substring(1, value.length() - 1);
        } else
            return null;
    }
}
