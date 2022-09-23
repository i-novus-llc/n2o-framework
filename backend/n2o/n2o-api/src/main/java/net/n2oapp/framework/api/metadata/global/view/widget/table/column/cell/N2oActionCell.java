package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;

/**
 * Исходная модель ячейки с действием
 */
@Getter
@Setter
public class N2oActionCell extends N2oAbstractCell implements ModelAware {
    private String actionId;
    private N2oAction n2oAction;
    private ReduxModel model;

    @JsonProperty
    private Action action;
}
