package net.n2oapp.framework.api.metadata.meta.control.masked;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель поля {@code <masked-input>}
 */
@Getter
@Setter
public class MaskedInput extends MaskedControl {
    @JsonProperty
    private String mask;
    @JsonProperty
    private String measure;
    @JsonProperty
    private Boolean clearOnBlur;
    @JsonProperty
    private String autocomplete;
}
