package net.n2oapp.framework.api.metadata.meta.control.masked;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.control.Control;

/**
 * Клиентская модель поля ввода с маской
 */
@Getter
@Setter
public abstract class MaskedControl extends Control {
    @JsonProperty
    private String invalidText;
}
