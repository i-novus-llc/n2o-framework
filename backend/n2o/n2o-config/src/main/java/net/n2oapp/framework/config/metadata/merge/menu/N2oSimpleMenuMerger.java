package net.n2oapp.framework.config.metadata.merge.menu;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух меню(<nav>/<extra-menu>)
 */
@Component
public class N2oSimpleMenuMerger implements BaseSourceMerger<N2oSimpleMenu> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSimpleMenu.class;
    }

    @Override
    public N2oSimpleMenu merge(N2oSimpleMenu ref, N2oSimpleMenu source) {
        setIfNotNull(source::setId, source::getId, ref::getId);
        setIfNotNull(source::setSrc, source::getSrc, ref::getSrc);
        addIfNotNull(ref, source, N2oSimpleMenu::setMenuItems, N2oSimpleMenu::getMenuItems);
        mergeExtAttributes(ref, source);
        return source;
    }

}