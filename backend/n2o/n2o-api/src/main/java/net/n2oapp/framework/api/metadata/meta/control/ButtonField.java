package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.Map;

/**
 * Клиентская модель компонента ButtonField
 */
@Getter
@Setter
public class ButtonField extends Field {
    @JsonProperty
    private String color;
    @JsonProperty
    private String icon;
    @JsonProperty
    private Action action;
    @JsonProperty
    private String hint;
    @JsonProperty
    private String hintPosition;
    @JsonProperty
    private Confirm confirm;
    @JsonProperty
    private String validate;
    @JsonProperty
    private String validatedWidgetId;

    @JsonProperty
    private String url;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
}
