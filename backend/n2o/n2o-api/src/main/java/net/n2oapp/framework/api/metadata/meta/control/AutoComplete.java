package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Клиентская модель компонента ввода текста с автоподбором
 */
@Getter
@Setter
public class AutoComplete extends ListControl {
    @JsonProperty
    private Boolean tags;
    @JsonProperty
    private Integer maxTagTextLength;
}
