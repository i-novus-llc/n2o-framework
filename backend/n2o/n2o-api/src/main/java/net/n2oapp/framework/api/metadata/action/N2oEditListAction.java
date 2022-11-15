package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.action.editlist.ListOperationType;

/**
 * Исходная модель действия редактирования записи списка
 */
@Getter
@Setter
public class N2oEditListAction extends N2oAbstractAction {

    private ListOperationType operation;
    private String primaryKey;
    private String datasourceId;
    private ReduxModel model;
    private String listFieldId;
    private String itemDatasourceId;
    private ReduxModel itemModel;
    private String itemFieldId;

}
