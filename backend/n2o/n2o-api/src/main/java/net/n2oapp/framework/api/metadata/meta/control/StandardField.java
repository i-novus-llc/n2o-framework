package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Клиентская модель стандартного поля
 */
@Getter
@Setter
public class StandardField<T extends Control> extends Field {
    @JsonProperty("control")
    protected T control;
}
