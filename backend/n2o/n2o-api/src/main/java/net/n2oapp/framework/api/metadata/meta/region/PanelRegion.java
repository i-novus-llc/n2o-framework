package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель региона в виде панелей.
 */
@Getter
@Setter
public class PanelRegion extends Region {
    @JsonProperty
    private String color;
    @JsonProperty
    private String icon;
    @JsonProperty
    private Boolean hasTabs;
    @JsonProperty
    private String headerTitle;
    @JsonProperty
    private String footerTitle;
    @JsonProperty
    private Boolean open;
    @JsonProperty
    private Boolean collapsible;
    @JsonProperty
    private Boolean fullScreen;
    @JsonProperty
    private Boolean header;
}

