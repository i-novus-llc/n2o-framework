package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;

import java.util.List;

/**
 * Клиентская модель кастомного поля ввода
 */
@Getter
@Setter
public class CustomField extends Field {
    @JsonProperty("control")
    protected Component control;
    @JsonProperty("controls")
    protected List<Component> controls;
}
