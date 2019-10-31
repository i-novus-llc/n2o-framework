package net.n2oapp.framework.api.metadata.meta.control;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель чекбокса
 */
@Getter
@Setter
public class Checkbox extends Control {
    @JsonProperty
    private String label;
    @JsonProperty
    private Boolean readOnly;
    @JsonProperty
    private Boolean defaultUnchecked;
}
