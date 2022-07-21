package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;

import java.util.List;
import java.util.Map;


/**
 * Клиентская модель компонента ввода текста с автоподбором
 */
@Getter
@Setter
public class AutoComplete extends Control {
    @JsonProperty
    private ClientDataProvider dataProvider;
    @JsonProperty
    private String valueFieldId;
    @JsonProperty
    private List<Map<String, Object>> data;
    @JsonProperty
    private Boolean tags;
    @JsonProperty
    private Integer maxTagTextLength;
}
