package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.list.RadioGroupType;

/**
 * Клиентская модель компонента радио кнопок
 */
@Getter
@Setter
public class RadioGroup extends ListControl {
    @JsonProperty
    private Boolean inline;
    @JsonProperty
    private RadioGroupType type;
    @JsonProperty
    private Integer size;

}
