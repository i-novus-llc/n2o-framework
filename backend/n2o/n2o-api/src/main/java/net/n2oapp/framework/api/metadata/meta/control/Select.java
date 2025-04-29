package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.list.ListTypeEnum;

/**
 * Клиентская модель компонента выбора из выпадающего списка
 */
@Getter
@Setter
public class Select extends ListControl {
    @JsonProperty
    private ListTypeEnum type;
    @JsonProperty
    private Boolean cleanable;
    @JsonProperty
    private String selectFormat;
    @JsonProperty
    private String selectFormatOne;
    @JsonProperty
    private String selectFormatFew;
    @JsonProperty
    private String selectFormatMany;
    @JsonProperty
    private String descriptionFieldId;
}
