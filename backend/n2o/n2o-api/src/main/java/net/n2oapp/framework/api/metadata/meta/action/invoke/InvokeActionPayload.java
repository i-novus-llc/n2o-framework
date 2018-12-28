package net.n2oapp.framework.api.metadata.meta.action.invoke;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;

/**
 * Клиентская модель коомпонента invoke-action
 */
@Getter
@Setter
public class InvokeActionPayload implements ActionPayload {
    @JsonProperty
    private String widgetId;
    @JsonProperty
    private WidgetDataProvider dataProvider;
    @JsonProperty
    private String modelLink;
}
