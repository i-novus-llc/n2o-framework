package net.n2oapp.framework.api.metadata.meta.action.invoke;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

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
    private ReduxModelEnum model;
    @JsonProperty
    private String widgetId;
    @JsonProperty
    private String pageId;
}
