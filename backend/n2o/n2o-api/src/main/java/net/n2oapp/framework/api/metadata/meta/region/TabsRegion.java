package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель региона в виде вкладок.
 */
@Getter
@Setter
public class TabsRegion extends Region {
    @JsonProperty
    private List<Tab> tabs;
    @JsonProperty
    private Boolean alwaysRefresh;
    @JsonProperty
    private Boolean lazy;
    private String activeParam;
    private Boolean routable;

    @Override
    @JsonProperty("tabs")
    public List<? extends Item> getItems() {
        return super.getItems();
    }

    @Getter
    @Setter
    public static class Tab extends Item {
        @JsonProperty
        private String icon;
    }
}
