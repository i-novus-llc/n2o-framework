package net.n2oapp.framework.api.metadata.menu;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;

/**
 * Меню навигации
 */
public abstract class N2oMenu extends N2oMetadata {

    @Override
    public String getPostfix() {
        return "menu";
    }

    @Override
    public Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oMenu.class;
    }
}
