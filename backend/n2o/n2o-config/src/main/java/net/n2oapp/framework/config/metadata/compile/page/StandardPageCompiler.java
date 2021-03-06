package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.IndexScope;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.widget.PageWidgetsScope;
import org.springframework.stereotype.Component;

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
        return compilePage(source, new StandardPage(), context, p, source.getItems(), null);
    }

    @Override
    protected void initRegions(N2oStandardPage source, StandardPage page, CompileProcessor p, PageContext context,
                               PageScope pageScope, PageRoutes pageRoutes, PageWidgetsScope pageWidgetsScope) {
        Map<String, List<Region>> regions = new HashMap<>();
        initRegions(source.getItems(), regions, "single", context, p, pageScope, pageRoutes, pageWidgetsScope, new IndexScope());
        page.setRegions(regions);
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.page.standard.src";
    }
}
