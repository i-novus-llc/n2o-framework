package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Клиентская модель поля для ввода текста
 */
@Getter
@Setter
public class InputText extends Control {
    @JsonProperty
    private Integer length;
    @JsonProperty
    private String placeholder;
    @JsonProperty
    private Object min;
    @JsonProperty
    private Object max;
    @JsonProperty
    private String step;
    @JsonProperty
    private Boolean showButtons;
}
