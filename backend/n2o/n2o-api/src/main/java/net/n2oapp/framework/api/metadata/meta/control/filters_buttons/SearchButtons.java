package net.n2oapp.framework.api.metadata.meta.control.filters_buttons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.control.Field;

/**
 * Клиентская модель компонента SearchButtons (кнопки фильтра)
 */
@Getter
@Setter
public class SearchButtons extends Field {

    @JsonProperty
    private SearchButton search;

    @JsonProperty
    private ClearButton clear;
}
