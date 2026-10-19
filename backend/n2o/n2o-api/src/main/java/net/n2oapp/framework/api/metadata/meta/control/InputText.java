package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клиентская модель поля для ввода текста
 */
@Getter
@Setter
public class InputText extends Control {
    @JsonProperty
    private Integer length;
    @JsonProperty
    private Serializable min;
    @JsonProperty
    private Serializable max;
    @JsonProperty
    private String step;
    @JsonProperty
    private String measure;
    @JsonProperty
    private Boolean showButtons;
    @JsonProperty
    private Integer precision;
    @JsonProperty
    private String autocomplete;
}
