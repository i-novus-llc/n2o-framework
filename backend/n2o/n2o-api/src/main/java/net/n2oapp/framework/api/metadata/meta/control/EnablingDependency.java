package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель зависимости доступности
 */
@Getter
@Setter
public class EnablingDependency extends ControlDependency {
    @JsonProperty
    private String message;
}
