package net.n2oapp.framework.api.metadata.meta.widget.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

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
