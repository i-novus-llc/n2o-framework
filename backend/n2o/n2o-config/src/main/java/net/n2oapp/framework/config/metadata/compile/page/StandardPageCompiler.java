package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Компиляция стандартной страницы с регионами
 */
@Component
public class StandardPageCompiler extends BasePageCompiler<N2oStandardPage, StandardPage> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardPage.class;
    }

    @Override
    public StandardPage compile(N2oStandardPage source, PageContext context, CompileProcessor p) {
        return compilePage(source, new StandardPage(), context, p, source.getRegions());
    }

    @Override
    protected void initRegions(N2oStandardPage source, StandardPage page, CompileProcessor p,
                               PageContext context, PageScope pageScope) {
        Map<String, List<Region>> regionMap = new HashMap<>();
        if (source.getRegions() != null) {
            IndexScope index = new IndexScope();
            for (N2oRegion n2oRegion : source.getRegions()) {
                Region region = p.compile(n2oRegion, context, index, pageScope);
                String place = p.cast(n2oRegion.getPlace(), "single");
                if (regionMap.get(place) != null) {
                    regionMap.get(place).add(region);
                } else {
                    List<Region> regionList = new ArrayList<>();
                    regionList.add(region);
                    regionMap.put(place, regionList);
                }
            }
            page.setRegions(regionMap);
        }
    }

    @Override
    protected String getPropertyPageSrc() {
        return "n2o.api.page.standard.src";
    }
}
