package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oTopLeftRightPage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Компиляция страницы с тремя регионами
 * @deprecated Устаревшая компиляция для обратной совместимости. Используйте {@link StandardPageCompiler}
 */
@Deprecated(since = "7.29")
@Component
public class TopLeftRightPageCompiler extends BasePageCompiler<N2oTopLeftRightPage, StandardPage> {

    public Class<? extends Source> getSourceClass() {
        return N2oTopLeftRightPage.class;
    }

    @Override
    public StandardPage compile(N2oTopLeftRightPage source, PageContext context, CompileProcessor p) {
        source.adapterV4();
        return compilePage(source, new StandardPage(), context, p, null);
    }

    @Override
    protected Map<String, List<Region>> initRegions(N2oTopLeftRightPage source, StandardPage page, CompileProcessor p,
                                                    PageContext context, Object... scopes) {
        Map<String, List<Region>> regions = new HashMap<>();
        initRegions(source.getItems(), regions, "single", context, p, scopes);
        return regions;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.page.standard.src";
    }
}
