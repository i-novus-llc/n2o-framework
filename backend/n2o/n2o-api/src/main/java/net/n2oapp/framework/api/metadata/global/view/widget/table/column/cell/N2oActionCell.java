package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;

/**
 * Абстрактная ячейка с действием
 */
@Getter
@Setter
public class N2oActionCell extends N2oAbstractCell implements ModelAware {
    private String actionId;
    private N2oAction action;
    @JsonProperty("action")
    private Action compiledAction;
    private ReduxModel model;
}
