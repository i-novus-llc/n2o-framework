package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель календаря
 */
@Getter
@Setter
public class DatePicker extends Control {
    @JsonProperty("dateFormat")
    private String dateFormat;
    @JsonProperty("max")
    private String max;
    @JsonProperty("min")
    private String min;
    @JsonProperty("popupPlacement")
    private String popupPlacement;
    @JsonProperty("outputFormat")
    private String outputFormat;
}
