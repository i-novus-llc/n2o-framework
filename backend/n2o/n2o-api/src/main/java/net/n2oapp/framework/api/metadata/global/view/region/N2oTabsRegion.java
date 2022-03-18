package net.n2oapp.framework.api.metadata.global.view.region;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.RegionItem;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Модель региона в виде вкладок
 */
@Getter
@Setter
public class N2oTabsRegion extends N2oRegion implements RegionItem {
    private Boolean alwaysRefresh;
    private Boolean lazy;
    private String activeParam;
    private Boolean routable;
    private Boolean hideSingleTab;
    private String maxHeight;
    private Boolean scrollbar;
    private Tab[] tabs;

    @Getter
    @Setter
    public static class Tab implements Source, ExtensionAttributesAware, RegionItem {
        private String id;
        private String name;
        private SourceComponent[] content;
        @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
        @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
        private Map<N2oNamespace, Map<String, String>> extAttributes;

        @Override
        public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
            if (content != null) {
                if (!ids.containsKey(prefix))
                    ids.put(prefix, 1);
                for (SourceComponent component : content) {
                    if (component instanceof RegionItem)
                        ((RegionItem) component).collectWidgets(result, ids, prefix);
                }
            }
        }
    }

    @Deprecated
    public void setWidgets(N2oWidget[] widgets) {
        if (widgets != null) {
            Tab[] tabs = new Tab[widgets.length];
            for (int i = 0; i < widgets.length; i++) {
                Tab tab = new Tab();
                tab.setName(widgets[i].getName());
                tab.setContent(new SourceComponent[]{widgets[i]});
                tabs[i] = tab;
            }
            this.tabs = tabs;
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
