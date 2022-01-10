package net.n2oapp.framework.api.metadata.meta.widget.form;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetComponent;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель компонента формы
 */
@Getter
@Setter
public class FormWidgetComponent extends WidgetComponent {
    @JsonProperty
    private List<FieldSet> fieldsets;
    @JsonProperty
    @Deprecated
    private Map<String, List<Validation>> validation;
    @JsonProperty
    private String modelPrefix;
    @JsonProperty
    private Boolean prompt;
}
