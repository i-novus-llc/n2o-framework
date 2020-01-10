package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель группы чекбоксов
 */
@Getter
@Setter
public class CheckboxGroup extends ListControl {
    @JsonProperty
    private Boolean inline;
    @JsonProperty
    private String type;
    @JsonProperty
    private Map<String, String> style;
}

