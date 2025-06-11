package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Связывание региона `<tabs>` с данными
 */
@Component
public class TabsRegionBinder extends BaseRegionBinder<TabsRegion> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return TabsRegion.class;
    }

    @Override
    public TabsRegion bind(TabsRegion compiled, BindProcessor p) {
        if (!CollectionUtils.isEmpty(compiled.getItems()))
            compiled.getItems().forEach(tab -> bindTab(tab, p));
        return compiled;
    }

    private void bindTab(TabsRegion.Tab tab, BindProcessor p) {
        if (CollectionUtils.isEmpty(tab.getContent()))
            return;
        tab.getContent().forEach(p::bind);
    }
}
