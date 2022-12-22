package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента ввода текста с выбором из выпадающего списка
 */
@Getter
@Setter
public class InputSelect extends ListControl {
    @JsonProperty
    private Boolean multiSelect;
    @JsonProperty
    private Boolean hasCheckboxes;
    @JsonProperty
    private Boolean resetOnBlur;
    @JsonProperty
    private String descriptionFieldId;
    @JsonProperty
    private Integer maxTagTextLength;
    @JsonProperty
    private Integer throttleDelay;
    @JsonProperty
    private Integer searchMinLength;
}
