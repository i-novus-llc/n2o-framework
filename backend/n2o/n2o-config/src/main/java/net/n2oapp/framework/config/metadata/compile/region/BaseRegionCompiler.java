package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.region.RoutableRegion;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

public abstract class BaseRegionCompiler<D extends Region, S extends N2oRegion> extends ComponentCompiler<D, S, PageContext> {

    protected D build(D compiled, S source, CompileProcessor p) {
        compileComponent(compiled, source, p);
        compiled.setId(castDefault(source.getId(), createId(p)));

        return compiled;
    }

    protected abstract String createId(CompileProcessor p);

    protected String createId(String regionName, CompileProcessor p) {
        IndexScope index = p.getScope(IndexScope.class);

        return regionName + (nonNull(index) ? index.get() : "");
    }

    protected List<CompiledRegionItem> initContent(SourceComponent[] items,
                                                   PageContext context,
                                                   CompileProcessor p,
                                                   Source source) {
        if (isNull(items) || items.length == 0)
            return null;
        List<CompiledRegionItem> content = new ArrayList<>();
        ComponentScope componentScope = new ComponentScope(source);
        BasePageUtil.resolveRegionItems(
                items,
                item -> content.add(p.compile(item, context, componentScope)),
                item -> content.add(p.compile(item, context, componentScope))
        );

        return content;
    }

    protected void compileRoute(RoutableRegion source, String regionId, String property, CompileProcessor p) {
        Boolean routable = castDefault(
                source.getRoutable(),
                () -> p.resolve(property(property), Boolean.class)
        );
        PageRoutes routes = p.getScope(PageRoutes.class);
        if (isNull(routes) || !Boolean.TRUE.equals(routable))
            return;
        String activeParam = castDefault(source.getActiveParam(), regionId);
        routes.addQueryMapping(
                activeParam,
                Redux.dispatchSetActiveRegionEntity(regionId, activeParam),
                Redux.createActiveRegionEntityLink(regionId)
        );
    }
}
