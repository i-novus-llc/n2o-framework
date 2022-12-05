package net.n2oapp.framework.api.metadata.event;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;

/**
 * Исходная модель события изменения модели данных
 */
@Getter
@Setter
public class N2oOnChangeEvent extends N2oAbstractEvent implements ActionsAware, DatasourceIdAware, ModelAware {
    private String datasourceId;
    private ReduxModel model;
    private String fieldId;
    private String actionId;
    private N2oAction[] actions;
}
