package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.region.scrollspy.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Связывание региона `<scrollspy>` с данными
 */
@Component
public class ScrollspyRegionBinder extends BaseRegionBinder<ScrollspyRegion> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ScrollspyRegion.class;
    }

    @Override
    public ScrollspyRegion bind(ScrollspyRegion compiled, BindProcessor p) {
        if (!CollectionUtils.isEmpty(compiled.getMenu()))
            compiled.getMenu().forEach(menu -> bindMenu(menu, p));
        return compiled;
    }

    private void bindMenu(ScrollspyElement compiled, BindProcessor p) {
        if (compiled instanceof SingleScrollspyElement scrollspyElement) {
            scrollspyElement.getContent().forEach(p::bind);
        } else if (compiled instanceof GroupScrollspyElement group) {
            group.getGroup().forEach(e -> bindMenu(e, p));
        } else if (compiled instanceof MenuScrollspyElement menu)
            menu.getMenu().forEach(e -> bindMenu(e, p));
    }
}
