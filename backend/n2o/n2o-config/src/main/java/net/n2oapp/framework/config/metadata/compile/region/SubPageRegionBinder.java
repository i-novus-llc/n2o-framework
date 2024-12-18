package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.region.SubPageRegion;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание региона `<sub-page>` с данными
 */
@Component
public class SubPageRegionBinder implements BaseMetadataBinder<SubPageRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return SubPageRegion.class;
    }

    @Override
    public SubPageRegion bind(SubPageRegion compiled, BindProcessor p) {
        for (SubPageRegion.Page page : compiled.getPages())
            page.setUrl(p.resolveUrl(page.getUrl()));
        return compiled;
    }
}
