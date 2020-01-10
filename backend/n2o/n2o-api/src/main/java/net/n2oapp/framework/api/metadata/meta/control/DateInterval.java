package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента ввода интервала дат
 */
@Getter
@Setter
public class DateInterval extends Control {
    @JsonProperty
    private String dateFormat;
    @JsonProperty
    private String timeFormat;
    @JsonProperty
    private String max;
    @JsonProperty
    private String min;
    @JsonProperty
    private Boolean utc;
    @JsonProperty
    private String outputFormat;
}
