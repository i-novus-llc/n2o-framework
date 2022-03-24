package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oLeftRightPage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Компиляция страницы с правыми и левыми регионами
 */
@Component
public class LeftRightPageCompiler extends BasePageCompiler<N2oLeftRightPage, StandardPage> {
    @Override
    public StandardPage compile(N2oLeftRightPage source, PageContext context, CompileProcessor p) {
        StandardPage page = new StandardPage();
        if ((source.getLeftWidth() != null && !source.getLeftWidth().isEmpty()) ||
                (source.getRightWidth() != null && !source.getRightWidth().isEmpty()))
            page.setWidth(page.new RegionWidth(source.getLeftWidth(), source.getRightWidth()));
        return compilePage(source, page, context, p, null);
    }

    @Override
    protected Map<String, List<Region>> initRegions(N2oLeftRightPage source, StandardPage page,
                                                    CompileProcessor p, PageContext context, Object... scopes) {
        Map<String, List<Region>> regions = new HashMap<>();
        initRegions(source.getLeft(), regions, "left", context, p, scopes);
        initRegions(source.getRight(), regions, "right", context, p, scopes);
        return regions;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLeftRightPage.class;
    }

    @Override
    protected String getSrcProperty() {
        return "n2o.api.page.left_right.src";
    }
}
