package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionOptions;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRoteScope;
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
        ParentRoteScope routeScope = p.getScope(ParentRoteScope.class);
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
        initPathMapping(linkAction, source, p);
        return linkAction;
    }

    private void initPathMapping(LinkAction compiled, N2oAnchor source, CompileProcessor p) {
        WidgetScope scope = p.getScope(WidgetScope.class);
        if (scope == null
                || scope.getClientWidgetId() == null
                || p.getScope(ComponentScope.class).unwrap(ModelAware.class) == null)
            return;
        ReduxModel model = p.getScope(ComponentScope.class).unwrap(ModelAware.class).getModel();
        Map<String, BindLink> pathMapping = new StrictMap<>();
        List<String> pathParams = RouteUtil.getParams(source.getHref());
        for (String pathParam : pathParams) {
            pathMapping.put(pathParam, Redux.createBindLink(scope.getClientWidgetId(), p.cast(model, ReduxModel.RESOLVE), pathParam));
        }
        compiled.getOptions().setPathMapping(pathMapping);
    }
}
