package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель ползунка
 */
@Getter
@Setter
public class Slider extends ListControl{
    @JsonProperty
    private Boolean multiple;
    @JsonProperty
    private Boolean vertical;
    @JsonProperty
    private String tooltipFormatter;
    @JsonProperty
    private Integer min;
    @JsonProperty
    private Integer max;
    @JsonProperty
    private Integer step;
    @JsonProperty
    private Boolean showTooltip;
    @JsonProperty
    private String tooltipPlacement;
    @JsonProperty
    private Boolean disabled;
}
