package net.n2oapp.framework.api.metadata.meta.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.ActionAware;

/**
 * Клиентская модель события изменения модели данных
 */
@Getter
@Setter
public class OnChangeEvent extends Event implements ActionAware {
    @JsonProperty
    private String type;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModel model;
    @JsonProperty
    private String field;
    @JsonProperty
    private Action action;
}
