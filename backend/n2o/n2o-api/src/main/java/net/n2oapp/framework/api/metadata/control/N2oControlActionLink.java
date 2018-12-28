package net.n2oapp.framework.api.metadata.control;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

import java.io.Serializable;

/**
 * Абстрактный класс для button и link в actions контрола
 */
public abstract class N2oControlActionLink implements Serializable {

    private String label;
    private String id;
    private String key;
    @JsonIgnore
    private N2oAction event;
    private String eventId;
    private String fieldId;

    private N2oFieldCondition visibilityCondition;
    private N2oFieldCondition enablingCondition;
    private String actionAsString;

    public N2oFieldCondition getVisibilityCondition() {
        return visibilityCondition;
    }

    public void setVisibilityCondition(N2oFieldCondition visibilityCondition) {
        this.visibilityCondition = visibilityCondition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public N2oFieldCondition getEnablingCondition() {
        return enablingCondition;
    }

    public void setEnablingCondition(N2oFieldCondition enablingCondition) {
        this.enablingCondition = enablingCondition;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public N2oAction getEvent() {
        return event;
    }

    public void setEvent(N2oAction event) {
        this.event = event;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }


}
