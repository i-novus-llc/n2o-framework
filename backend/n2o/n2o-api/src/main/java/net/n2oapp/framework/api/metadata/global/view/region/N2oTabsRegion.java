package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

/**
 * Модель региона в виде вкладок
 */
@Getter
@Setter
public class N2oTabsRegion extends N2oAbstractRegion {
    private Boolean alwaysRefresh;
    private Boolean lazy;
    private String activeParam;
    private Boolean routable;
    private N2oWidget[] widgets;
    private Tab[] tabs;

    @Getter
    @Setter
    public static class Tab implements Source {
        private N2oWidget[] widgets;
        private N2oAbstractRegion[] regions;
    }

    @Override
    public String getAlias() {
        return "tab";
    }
}
