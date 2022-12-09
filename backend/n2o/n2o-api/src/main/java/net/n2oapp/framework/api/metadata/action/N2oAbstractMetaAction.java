package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Абстрактное действие, содержащее стандартные саги
 */
@Getter
@Setter
public abstract class N2oAbstractMetaAction extends N2oAbstractAction {
    private Boolean closeOnSuccess;
    private Boolean doubleCloseOnSuccess;
    private Boolean closeOnFail;
    private String redirectUrl;
    private Target redirectTarget;
    private Boolean refreshOnSuccess;
    private String[] refreshDatasourceIds;

    @Deprecated
    public String getRefreshWidgetId() {
        return refreshDatasourceIds != null && refreshDatasourceIds.length > 0 ? refreshDatasourceIds[0] : null;
    }

    @Deprecated
    public void setRefreshWidgetId(String refreshWidgetId) {
        this.refreshDatasourceIds = new String[] {refreshWidgetId};
    }
}
