package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.BasePageUtil;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.ComponentCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.PageWidgetsScope;

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

    protected List<Compiled> initContent(SourceComponent[] items, IndexScope index, PageWidgetsScope pageWidgetsScope,
                                         PageContext context, CompileProcessor p) {
        if (items == null || items.length == 0)
            return null;

        List<Compiled> content = new ArrayList<>();
        BasePageUtil.resolveRegionItems(items,
                item -> content.add(p.compile(item, context, p, index)),
                // TODO - необходимо учесть случай, когда виджет вне страницы (PageScope == null)
                item -> pageWidgetsScope.getWidgets().keySet().stream()
                        .filter(k -> k.endsWith((item).getId())).findFirst()
                        .ifPresent(s -> content.add(pageWidgetsScope.getWidgets().get(s))));
        return content;
    }
}
