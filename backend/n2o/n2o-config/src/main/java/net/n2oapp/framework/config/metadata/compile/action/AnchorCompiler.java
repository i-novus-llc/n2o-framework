package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;


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
        initDefaults(source, context, p);
        LinkActionImpl linkAction = new LinkActionImpl();
        source.setSrc(p.cast(source.getSrc(),
                p.resolve(Placeholders.property("n2o.api.action.link.src"), String.class)));
        linkAction.setType(p.resolve(property("n2o.api.action.link.type"), String.class));

        compileAction(linkAction, source, p);
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
        Target sourceTarget = source.getTarget();
        String path = source.getHref();
        if (!StringUtils.isLink(source.getHref())) {
            if (Target.self.equals(sourceTarget) || Target.newWindow.equals(sourceTarget)) {
                if (RouteUtil.isApplicationUrl(path)) {
                    path = RouteUtil.normalize(path);
                }
            } else {
                path = RouteUtil.absolute(source.getHref(), routeScope != null ? routeScope.getUrl() : null);
            }
        }
        initUrl(linkAction, path, source, routeScope, p);

        Target target = p.cast(source.getTarget(), Target.self);
        linkAction.setTarget(target);
        linkAction.setRestore(Boolean.TRUE);
        PageRoutes pageRoutes = p.getScope(PageRoutes.class);
        if (pageRoutes != null && Target.application.equals(source.getTarget())) {
            PageRoutes.Route pageRoute = new PageRoutes.Route(path);
            pageRoute.setIsOtherPage(true);
            pageRoutes.addRoute(pageRoute);
        }
        return linkAction;
    }

    private void initUrl(LinkActionImpl linkAction, String path, N2oAnchor source, ParentRouteScope routeScope,
                         CompileProcessor p) {
        String resolvedPath = p.resolveJS(path);
        linkAction.setUrl(resolvedPath);
        if (StringUtils.isJs(resolvedPath)) {
            String datasourceId = p.cast(source.getDatasourceId(), getLocalDatasourceId(p));
            ReduxModel reduxModel = p.cast(source.getModel(), getLocalModel(p));
            if (datasourceId == null) {
                throw new N2oException("Datasource not found for action <a> with linked href " + source.getHref());
            }
            linkAction.getPayload().setModelLink(new ModelLink(reduxModel, getClientDatasourceId(datasourceId, p)).getBindLink());
        }
        else
            initMappings(linkAction, source, p, routeScope);
    }

    private void initMappings(LinkAction compiled, N2oAnchor source, CompileProcessor p, ParentRouteScope routeScope) {
        Map<String, ModelLink> pathMapping = initParentRoutePathMappings(routeScope, compiled.getUrl());
        Map<String, ModelLink> queryMapping = new StrictMap<>();
        initMappings(source.getPathParams(), source.getQueryParams(), pathMapping, queryMapping, p);

        compiled.setQueryMapping(queryMapping);
        compiled.setPathMapping(pathMapping);
    }
}
