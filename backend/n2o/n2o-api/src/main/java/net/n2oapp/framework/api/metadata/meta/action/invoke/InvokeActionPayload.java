package net.n2oapp.framework.api.metadata.meta.action.invoke;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;

/**
 * Клиентская модель компонента invoke-action
 */
@Getter
@Setter
public class InvokeActionPayload implements ActionPayload {
    @JsonProperty
    private ClientDataProvider dataProvider;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModel model;
    @JsonProperty
    private String widgetId;
    @JsonProperty
    private String pageId;
}
