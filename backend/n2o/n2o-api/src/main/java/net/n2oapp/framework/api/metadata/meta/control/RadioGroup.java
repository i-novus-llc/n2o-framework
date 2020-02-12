package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента радио кнопок
 */
@Getter
@Setter
public class RadioGroup extends ListControl {
    @JsonProperty
    private Boolean inline;
    @JsonProperty
    private String type;
    @JsonProperty
    private Integer size;

}
