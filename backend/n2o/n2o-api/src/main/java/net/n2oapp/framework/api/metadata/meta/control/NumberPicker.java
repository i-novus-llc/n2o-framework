package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента выбора числа из диапазона
 */
@Getter
@Setter
public class NumberPicker extends Control {
    @JsonProperty
    private Integer max;
    @JsonProperty
    private Integer min;
    @JsonProperty
    private Integer step;
}
