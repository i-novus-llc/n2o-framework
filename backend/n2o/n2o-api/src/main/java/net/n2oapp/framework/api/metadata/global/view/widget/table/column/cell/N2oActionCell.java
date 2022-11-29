package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;

/**
 * Исходная модель ячейки с действиями
 */
@Getter
@Setter
public abstract class N2oActionCell extends N2oAbstractCell implements ModelAware, ActionsAware {
    private String actionId;
    private N2oAction[] actions;
    private ReduxModel model;

    @JsonProperty
    private Action action;
}
