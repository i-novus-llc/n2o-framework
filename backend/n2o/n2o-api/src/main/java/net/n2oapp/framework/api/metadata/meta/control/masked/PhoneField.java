package net.n2oapp.framework.api.metadata.meta.control.masked;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель поля {@code <phone>}
 */
@Getter
@Setter
public class PhoneField extends MaskedControl {
    @JsonProperty
    private List<String> countries;
}
