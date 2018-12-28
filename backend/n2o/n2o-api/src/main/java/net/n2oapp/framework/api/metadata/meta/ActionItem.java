package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Клиентская модель menuItem
 */
@Deprecated
public class ActionItem implements Serializable {
    @JsonProperty
    private String actionId;
    @JsonProperty
    private Boolean context;
    @JsonProperty
    private String label;
    @JsonProperty
    private String eventId;

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public Boolean getContext() {
        return context;
    }

    public void setContext(Boolean context) {
        this.context = context;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
