package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента текста
 */
@Getter
@Setter
public class Text extends Field {
    @JsonProperty
    private String text;
    @JsonProperty
    private String format;
}
