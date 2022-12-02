package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента SearchButtons (кнопки фильтра)
 */
@Getter
@Setter
public class SearchButtons extends Control {
    @JsonProperty
    private String searchLabel;
    @JsonProperty
    private String resetLabel;
    @JsonProperty
    private Boolean fetchOnClear;
}
