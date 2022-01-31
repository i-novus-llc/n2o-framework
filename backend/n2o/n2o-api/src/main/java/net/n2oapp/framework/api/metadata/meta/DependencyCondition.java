package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель условия зависимости
 */
@Getter
@Setter
@NoArgsConstructor
public class DependencyCondition implements Compiled {
    @JsonProperty
    private String on;
    @JsonProperty
    private Object condition;
    @JsonProperty
    private DependencyConditionType type;
}