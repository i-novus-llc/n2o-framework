package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель компонента вывода html
 */
@Getter
@Setter
public class Html extends Field {
    @JsonProperty
    private String html;
}
