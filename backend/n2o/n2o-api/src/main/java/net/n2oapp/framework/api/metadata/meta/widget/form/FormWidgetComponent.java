package net.n2oapp.framework.api.metadata.meta.widget.form;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

import java.util.List;

/**
 * Клиентская модель компонента формы
 */
@Getter
@Setter
public class FormWidgetComponent extends WidgetComponent {
    @JsonProperty
    private List<FieldSet> fieldsets;
    @JsonProperty
    private String modelPrefix;
    @JsonProperty
    private Boolean prompt;
}
