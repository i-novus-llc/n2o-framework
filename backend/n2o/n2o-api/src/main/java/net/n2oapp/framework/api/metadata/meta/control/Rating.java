package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель поля для ввода и отображения рейтинга
 */
@Getter
@Setter
public class Rating extends Control {
    @JsonProperty
    private Integer max;
    @JsonProperty
    private Boolean half;
    @JsonProperty
    private Boolean showTooltip;

}
