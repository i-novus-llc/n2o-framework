package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель зависимости
 */
@Getter
@Setter
public class Condition implements Compiled {
    @JsonProperty
    private String expression;
    @JsonProperty
    private String modelLink;
}
