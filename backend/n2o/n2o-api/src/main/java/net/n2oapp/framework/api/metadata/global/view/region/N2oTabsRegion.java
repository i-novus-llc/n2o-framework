package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.Arrays;

/**
 * Модель региона в виде вкладок
 */
@Getter
@Setter
public class N2oTabsRegion extends N2oRegion {
    private Boolean alwaysRefresh;
    private Boolean lazy;
    private String activeParam;
    private Boolean routable;
    private Tab[] tabs;

    @Getter
    @Setter
    public static class Tab implements Source {
        private String name;
        private SourceComponent[] items;
    }

    public void setWidgets(N2oWidget[] widgets) {
        if (widgets != null) {
            Tab[] tabs = new Tab[widgets.length];
            for (int i = 0; i < widgets.length; i++) {
                Tab tab = new Tab();
                tab.setName(widgets[i].getName());
                tab.setItems(new SourceComponent[]{widgets[i]});
                tabs[i] = tab;
            }
            this.tabs = tabs;
        }
    }

    public N2oWidget[] getWidgets() {
        if (tabs != null)
            return Arrays.stream(tabs).map(t -> t.getItems()[0]).toArray(N2oWidget[]::new);
        return null;
    }

    @Override
    public String getAlias() {
        return "tab";
    }
}
