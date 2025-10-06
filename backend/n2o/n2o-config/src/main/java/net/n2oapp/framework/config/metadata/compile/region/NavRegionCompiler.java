package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oNavRegion;
import net.n2oapp.framework.api.metadata.menu.N2oAbstractMenuItem;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.NavRegion;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция региона {@code <nav>}
 */
@Component
public class NavRegionCompiler extends BaseRegionCompiler<NavRegion, N2oNavRegion> {

    @Override
    protected String getSrcProperty() {
        return "n2o.api.nav.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oNavRegion.class;
    }

    @Override
    public NavRegion compile(N2oNavRegion source, PageContext context, CompileProcessor p) {
        NavRegion compiled = new NavRegion();
        build(compiled, source, p);
        compiled.setDirection(castDefault(source.getDirection(),
                () -> p.resolve(property("n2o.api.nav.direction_type"), N2oNavRegion.DirectionTypeEnum.class)));
        compiled.setDatasource(getClientDatasourceId(source.getDatasourceId(), p));
        compiled.setModel(castDefault(source.getModel(), ReduxModelEnum.RESOLVE));
        compileItems(compiled, source, context, p);
        return compiled;
    }

    public void compileItems(NavRegion compiled, N2oNavRegion source, CompileContext<?, ?> context, CompileProcessor p) {
        List<CompiledRegionItem> items = new ArrayList<>();
        ComponentScope componentScope = new ComponentScope(source);
        IndexScope idx = new IndexScope();
        for (N2oAbstractMenuItem mi : source.getMenuItems()) {
            mi.setDatasourceId(castDefault(mi.getDatasourceId(), source.getDatasourceId()));
            mi.setModel(castDefault(mi.getModel(), compiled.getModel()));
            items.add(p.compile(mi, context, componentScope, idx));
        }
        compiled.setContent(items);
    }

    @Override
    protected String createId(CompileProcessor p) {
        return createId("nav", p);
    }
}