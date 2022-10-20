package net.n2oapp.framework.api.metadata.meta.action.switchaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

import java.util.Map;

/**
 * Полезная нагрузка действия switch
 */
@Getter
@Setter
public class SwitchActionPayload implements ActionPayload {
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModel model;
    @JsonProperty
    private String valueFieldId;
    @JsonProperty
    private Map<String, Action> cases;
    @JsonProperty("default")
    private Action defaultCase;
}
