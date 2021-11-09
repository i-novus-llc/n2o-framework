package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;

import java.util.Map;

/**
 * Абстрактная ячейка с действием
 */
@Getter
@Setter
public class N2oActionCell extends N2oAbstractCell implements ModelAware, ActionAware {
    private String actionId;
    private N2oAction n2oAction;
    private ReduxModel model;

    @JsonProperty
    private Action action;
    @JsonProperty
    private String url;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
}
