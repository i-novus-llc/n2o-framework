package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * Клиентская модель поля с маской для ввода текста
 */
@Getter
@Setter
public class MaskedInput extends Control {
    @JsonProperty
    private String mask;
    @JsonProperty
    private String placeholder;
}
