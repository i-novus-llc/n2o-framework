package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;

/**
 * Исходная модель региона с отслеживанием прокрутки
 */
@Getter
@Setter
public class N2oScrollspyRegion extends N2oRegion {

    private String title;
    private String active;
    private String placement;
    private Boolean headlines;
    private AbstractMenuItem[] menu;

    @Override
    public String getAlias() {
        return "scrollspy";
    }

    @Getter
    @Setter
    public static class MenuItem extends AbstractMenuItem {
        private SourceComponent[] content;
    }

    @Getter
    @Setter
    public static class SubMenuItem extends AbstractMenuItem {
        private AbstractMenuItem[] subMenu;
    }

    @Getter
    @Setter
    public static abstract class AbstractMenuItem {
        private String id;
        private String title;
    }
}
