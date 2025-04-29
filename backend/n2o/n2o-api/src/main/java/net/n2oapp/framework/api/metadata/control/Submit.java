package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePositionEnum;

@Getter
@Setter
public class Submit implements Source {
    private String operationId;
    private Boolean messageOnSuccess;
    private Boolean messageOnFail;
    private MessagePositionEnum messagePosition;
    private MessagePlacementEnum messagePlacement;
    private String messageWidgetId;
    private String route;
    private N2oParam[] pathParams;
    private N2oParam[] headerParams;
    private N2oFormParam[] formParams;
    private Boolean refreshOnSuccess;
    private String[] refreshDatasourceIds;
    private SubmitOnEnum submitOn;
    private Boolean submitAll;
    private Boolean clearCacheAfterSubmit;

    @Deprecated
    public String getRefreshWidgetId() {
        return refreshDatasourceIds == null ? null : refreshDatasourceIds[0];
    }

    @Deprecated
    public void setRefreshWidgetId(String refreshWidgetId) {
        this.refreshDatasourceIds = new String[]{refreshWidgetId};
    }
}
