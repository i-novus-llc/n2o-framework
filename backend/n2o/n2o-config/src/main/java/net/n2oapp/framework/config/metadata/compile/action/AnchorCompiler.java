package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
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
        initDefaults(source, p);
        LinkActionImpl linkAction = new LinkActionImpl();
        source.setSrc(castDefault(source.getSrc(),
                () -> p.resolve(Placeholders.property("n2o.api.action.link.src"), String.class)));
        linkAction.setType(p.resolve(property("n2o.api.action.link.type"), String.class));

        compileAction(linkAction, source, p);
        ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);

        String path = source.getHref();
        TargetEnum target = castDefault(source.getTarget(), TargetEnum.SELF);
        if (!StringUtils.isLink(source.getHref())) {
            if (TargetEnum.APPLICATION.equals(target)) {
                path = RouteUtil.absolute(source.getHref(), routeScope != null ? routeScope.getUrl() : null);
            }
            path = RouteUtil.normalize(path);
        }

        initUrl(linkAction, path, source, routeScope, p);
        linkAction.setTarget(target);
        return linkAction;
    }

    private void initUrl(LinkActionImpl linkAction, String path, N2oAnchor source, ParentRouteScope routeScope,
                         CompileProcessor p) {
        String resolvedPath = p.resolveJS(path);
        linkAction.setUrl(resolvedPath);
        if (StringUtils.isJs(resolvedPath)) {
            String datasourceId = castDefault(source.getDatasourceId(), () -> getLocalDatasourceId(p));
            ReduxModelEnum reduxModel = castDefault(source.getModel(), () -> getLocalModel(p));
            if (datasourceId == null) {
                throw new N2oException("Источник данных не найден для действия \"<a>\" со связанным 'href' " + source.getHref());
            }
            linkAction.getPayload().setModelLink(new ModelLink(reduxModel, getClientDatasourceId(datasourceId, p)).getLink());
        } else
            initMappings(linkAction, source, p, routeScope);
    }

    private void initMappings(LinkAction compiled, N2oAnchor source, CompileProcessor p, ParentRouteScope routeScope) {
        Map<String, ModelLink> pathMapping = initParentRoutePathMappings(routeScope, compiled.getUrl());
        Map<String, ModelLink> queryMapping = new LinkedHashMap<>();
        initMappings(source.getPathParams(), source.getQueryParams(), pathMapping, queryMapping, p);

        compiled.setQueryMapping(queryMapping);
        compiled.setPathMapping(pathMapping);
    }
}
