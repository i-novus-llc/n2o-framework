package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента ввода времени
 */
@Getter
@Setter
public class TimePicker extends Control {
    @JsonProperty
    private String prefix;
    @JsonProperty
    private String[] mode;
    @JsonProperty
    private String format;
    @JsonProperty
    private String timeFormat;
}
