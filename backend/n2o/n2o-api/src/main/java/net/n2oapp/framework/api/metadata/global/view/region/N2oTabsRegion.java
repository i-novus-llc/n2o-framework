package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.RegionItem;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Модель региона в виде вкладок
 */
@Getter
@Setter
public class N2oTabsRegion extends N2oRegion implements RegionItem, RoutableRegion {
    private Boolean alwaysRefresh;
    private Boolean lazy;
    private Boolean hideSingleTab;
    private String maxHeight;
    private Boolean scrollbar;
    private String datasourceId;
    private String activeTabFieldId;
    private Boolean routable;
    private String activeParam;
    private Tab[] tabs;

    @Getter
    @Setter
    public static class Tab implements Source, ExtensionAttributesAware, RegionItem {
        private String id;
        private String name;
        private String datasource;
        private String enabled;
        private String visible;
        private SourceComponent[] content;
        @ExtAttributesSerializer
        private Map<N2oNamespace, Map<String, String>> extAttributes;

        @Override
        public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
            if (content != null) {
                ids.putIfAbsent(prefix, 1);
                for (SourceComponent component : content) {
                    if (component instanceof RegionItem regionItem)
                        regionItem.collectWidgets(result, ids, prefix);
                }
            }
        }
    }

    @Deprecated
    public void setWidgets(N2oWidget[] widgets) {
        if (widgets != null) {
            Tab[] tabArray = new Tab[widgets.length];
            for (int i = 0; i < widgets.length; i++) {
                Tab tab = new Tab();
                tab.setContent(new SourceComponent[]{widgets[i]});
                tabArray[i] = tab;
            }
            this.tabs = tabArray;
        }
    }

    @Deprecated
    public N2oWidget[] getWidgets() {
        if (tabs != null)
            return Arrays.stream(tabs).map(t -> t.getContent()[0]).toArray(N2oWidget[]::new);
        return null;
    }

    @Override
    public String getAlias() {
        return "tab";
    }

    @Override
    public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
        if (tabs != null)
            for (Tab tab : tabs)
                tab.collectWidgets(result, ids, getAlias());
    }
}
