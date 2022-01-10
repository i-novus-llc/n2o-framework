package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public abstract class BaseRegionCompiler<D extends Region, S extends N2oRegion> extends ComponentCompiler<D, S, PageContext> {

    protected D build(D compiled, S source, CompileProcessor p) {
        compileComponent(compiled, source, null, p);
        compiled.setId(p.cast(source.getId(), createId(source.getPlace(), p)));
        return compiled;
    }

    protected abstract String createId(String regionPlace, CompileProcessor p);

    protected String createId(String regionPlace, String regionName, CompileProcessor p) {
        StringJoiner id = new StringJoiner("_");
        if (regionPlace != null)
            id.add(regionPlace);
        id.add(regionName);

        IndexScope index = p.getScope(IndexScope.class);
        if (index != null)
            id.add("" + index.get());
        return id.toString();
    }

    protected List<Compiled> initContent(SourceComponent[] items,
                                         PageContext context,
                                         CompileProcessor p,
                                         Source source) {
        if (items == null || items.length == 0)
            return null;

        List<Compiled> content = new ArrayList<>();
        ComponentScope componentScope = new ComponentScope(source);
        BasePageUtil.resolveRegionItems(items,
                item -> content.add(p.compile(item, context, componentScope)),
                item -> content.add(p.compile(item, context, componentScope)));
        return content;
    }
}
