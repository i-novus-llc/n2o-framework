package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

public abstract class BaseRegionCompiler<D extends Region, S extends N2oRegion> implements BaseSourceCompiler<D, S, PageContext> {

    protected abstract String getPropertyRegionSrc();

    protected D build(D compiled, S source, CompileProcessor p) {
        compiled.setSrc(p.cast(source.getSrc(), p.resolve(property(getPropertyRegionSrc()), String.class)));
        compiled.setProperties(p.mapAttributes(source));
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

    @SuppressWarnings("unchecked")
    protected <I extends Region.Item> List<I> initItems(N2oRegion source, CompileProcessor p, Class<I> itemClass) {
        List<I> items = new ArrayList<>();
        if (source.getWidgets() != null) {
            IndexScope index = new IndexScope(1);
            for (N2oWidget n2oWidget : source.getWidgets()) {
                Region.Item item = createItem(n2oWidget, index, p);
                PageScope pageScope = p.getScope(PageScope.class);
                if (pageScope != null) {
                    item.setWidgetId(pageScope.getGlobalWidgetId(n2oWidget.getId()));
                } else {
                    item.setWidgetId(n2oWidget.getId());
                }
                if (!itemClass.equals(item.getClass()))
                    throw new IllegalStateException();
                items.add((I) item);
            }
        }
        return items;
    }

    protected abstract Region.Item createItem(N2oWidget widget, IndexScope index, CompileProcessor p);

}
