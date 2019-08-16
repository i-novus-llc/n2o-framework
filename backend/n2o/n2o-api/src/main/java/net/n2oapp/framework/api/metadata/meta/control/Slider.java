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
    private boolean multiple;
    @JsonProperty
    private boolean vertical;
    @JsonProperty
    private String tooltipFormatter;
    @JsonProperty
    private int min;
    @JsonProperty
    private int max;
    @JsonProperty
    private int step;
    @JsonProperty
    private boolean showTooltip;
    @JsonProperty
    private String tooltipPlacement;
    @JsonProperty
    private boolean disabled;
}
