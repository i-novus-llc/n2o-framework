package net.n2oapp.framework.api.metadata.meta.control;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента многострочного ввода
 */
@Getter
@Setter
public class TextArea extends Control {
    @JsonProperty("rows")
    private Integer minRows;
    @JsonProperty
    private Integer maxRows;
}
