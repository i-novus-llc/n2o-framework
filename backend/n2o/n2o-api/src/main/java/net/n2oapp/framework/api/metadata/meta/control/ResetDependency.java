package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель зависимости
 */
@Getter
@Setter
public class ResetDependency extends ControlDependency {
    @JsonProperty
    private Boolean validate;
}
