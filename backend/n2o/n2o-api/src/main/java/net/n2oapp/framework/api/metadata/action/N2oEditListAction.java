package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.action.editlist.ListOperationTypeEnum;

/**
 * Исходная модель действия редактирования записи списка
 */
@Getter
@Setter
public class N2oEditListAction extends N2oAbstractAction {
    private ListOperationTypeEnum operation;
    private String primaryKey;
    private String datasourceId;
    private ReduxModelEnum model;
    private String listFieldId;
    private String itemDatasourceId;
    private ReduxModelEnum itemModel;
    private String itemFieldId;

}
