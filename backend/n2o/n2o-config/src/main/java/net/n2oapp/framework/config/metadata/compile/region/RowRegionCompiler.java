package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.region.*;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.RowRegion;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция региона {@code <row>}
 */
@Component
public class RowRegionCompiler extends BaseRegionCompiler<RowRegion, N2oRowRegion> {

    private static final String COLUMNS_PROPERTY = "n2o.api.region.row.columns";
    private static final String WRAP_PROPERTY = "n2o.api.region.row.wrap";
    private static final String ALIGN_PROPERTY = "n2o.api.region.row.align";
    private static final String JUSTIFY_PROPERTY = "n2o.api.region.row.justify";

    @Override
    public Class<N2oRowRegion> getSourceClass() {
        return N2oRowRegion.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.region.row.src";
    }

    @Override
    public RowRegion compile(N2oRowRegion source, PageContext context, CompileProcessor p) {
        RowRegion region = new RowRegion();
        build(region, source, p);
        region.setColumns(castDefault(source.getColumns(),
                () -> p.resolve(property(COLUMNS_PROPERTY), Integer.class)));
        region.setWrap(castDefault(source.getWrap(),
                () -> p.resolve(property(WRAP_PROPERTY), Boolean.class)));
        region.setAlign(castDefault(source.getAlign(),
                () -> p.resolve(property(ALIGN_PROPERTY), AlignEnum.class)));
        region.setJustify(castDefault(source.getJustify(),
                () -> p.resolve(property(JUSTIFY_PROPERTY), JustifyEnum.class)));
        region.setContent(initContent(source.getContent(), context, p, source));
        return region;
    }

    @Override
    protected List<CompiledRegionItem> initContent(SourceComponent[] items,
                                                   PageContext context,
                                                   CompileProcessor p,
                                                   Source source) {
        if (ArrayUtils.isEmpty(items))
            return Collections.emptyList();
        List<CompiledRegionItem> content = new ArrayList<>();
        ComponentScope componentScope = new ComponentScope(source);
        BasePageUtil.resolveRegionItems(
                items,
                region -> {
                    if (!(region instanceof N2oColRegion)) {
                        wrapInColRegion(new N2oRegion[]{region}, content, p, context, componentScope);
                    } else {
                        content.add(p.compile(region, context, componentScope));
                    }
                },
                widget -> wrapInColRegion(new N2oWidget[]{widget}, content, p, context, componentScope)
        );
        return content;
    }

    @Override
    protected String createId(CompileProcessor p) {
        return createId("row", p);
    }

    private static void wrapInColRegion(SourceComponent[] item, List<CompiledRegionItem> content, CompileProcessor p, PageContext context, ComponentScope componentScope) {
        N2oColRegion col = new N2oColRegion();
        col.setContent(item);
        content.add(p.compile(col, context, componentScope));
    }
}
