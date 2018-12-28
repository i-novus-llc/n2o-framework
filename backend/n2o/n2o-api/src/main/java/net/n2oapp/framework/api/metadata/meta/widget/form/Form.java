package net.n2oapp.framework.api.metadata.meta.widget.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Клиентская модель формы
 */
@Getter
@Setter
public class Form extends Widget<FormWidgetComponent> {

    public Form() {
        super(new FormWidgetComponent());
    }

    @JsonProperty("form")
    @Override
    public FormWidgetComponent getComponent() {
        return super.getComponent();
    }
}
