package net.n2oapp.framework.api.metadata.meta.action.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

/**
 * Полезная нагрузка условного действия
 */
@Getter
@Setter
public class ConditionActionPayload implements ActionPayload {
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModel model;
    @JsonProperty
    private String condition;
    @JsonProperty
    private Action success;
    @JsonProperty
    private Action fail;
}
