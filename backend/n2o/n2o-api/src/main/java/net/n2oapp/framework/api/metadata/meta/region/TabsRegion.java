package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Itemable;

import java.util.List;

/**
 * Клиентская модель региона в виде вкладок.
 */
@Getter
@Setter
public class TabsRegion extends Region implements Itemable<TabsRegion.Tab> {
    @JsonProperty
    private Boolean alwaysRefresh;
    @JsonProperty
    private Boolean lazy;
    @JsonProperty
    private Boolean hideSingleTab;
    @JsonProperty
    private Boolean fixed;
    @JsonProperty
    private Boolean scrollbar;
    @JsonProperty
    private String height;

    @JsonProperty("tabs")
    private List<Tab> items;

    @Getter
    @Setter
    public static class Tab extends RegionItem {
        @JsonProperty
        private String icon;
        @JsonProperty
        private Boolean opened;
    }
}
