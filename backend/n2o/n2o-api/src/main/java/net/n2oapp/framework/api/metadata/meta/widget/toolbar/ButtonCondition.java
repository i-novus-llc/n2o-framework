package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель зависимости между кнопкой и полем
 */
@Getter
@Setter
public class ButtonCondition implements Compiled {
    @JsonProperty
    private String expression;
    @JsonProperty
    private String modelLink;
}
