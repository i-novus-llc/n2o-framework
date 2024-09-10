package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.meta.control.ValidationType;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Клиентская модель региона в виде вкладок.
 */
@Getter
@Setter
public class TabsRegion extends Region implements CompiledRegionItem {
    @JsonProperty
    private Boolean alwaysRefresh;
    @JsonProperty
    private Boolean lazy;
    @JsonProperty
    private Boolean hideSingleTab;
    @JsonProperty
    private String maxHeight;
    @JsonProperty
    private Boolean scrollbar;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private String activeTabFieldId;
    @JsonProperty
    private Boolean routable;

    @JsonProperty("tabs")
    private List<Tab> items;

    @Getter
    @Setter
    public static class Tab implements CompiledRegionItem, JsonPropertiesAware {
        @JsonProperty
        private String id;
        @JsonProperty
        private String label;
        @JsonProperty
        private List<CompiledRegionItem> content;
        @JsonProperty
        private String icon;
        @JsonProperty
        private Boolean opened;
        @JsonProperty
        private String datasource;
        @JsonProperty
        private Object visible;
        @JsonProperty
        private Object enabled;
        @JsonProperty
        private Map<ValidationType, List<Condition>> conditions = new HashMap<>();
        private Map<String, Object> properties;

        @Override
        public void collectWidgets(List<Widget<?>> compiledWidgets) {
            collectWidgets(content, compiledWidgets);
        }
    }

    @Override
    public void collectWidgets(List<Widget<?>> compiledWidgets) {
        collectWidgets(items, compiledWidgets);
    }
}
