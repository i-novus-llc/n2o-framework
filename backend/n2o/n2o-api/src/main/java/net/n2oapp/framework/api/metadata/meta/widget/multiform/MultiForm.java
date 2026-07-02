package net.n2oapp.framework.api.metadata.meta.widget.multiform;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.FormWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;

/**
 * Клиентская модель виджета MultiForm
 */
@Getter
@Setter
public class MultiForm extends Widget<FormWidgetComponent> {
    @JsonProperty
    private Pagination paging;

    public MultiForm() {
        super(new FormWidgetComponent());
    }

    @JsonProperty("form")
    public FormWidgetComponent getForm() {
        return getComponent();
    }
}