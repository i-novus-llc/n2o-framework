package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.SubmitOn;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;

import java.io.Serializable;

/**
 *  Провайдер данных клиента
 */
@Getter
@Setter
public class N2oClientDataProvider implements Serializable {
    private String id;
    private String url;
    private N2oFormParam[] formParams;
    private N2oParam[] pathParams;
    private N2oParam[] headerParams;
    private N2oParam[] queryParams;
    private ReduxModel targetModel;
    @Deprecated
    private String targetWidgetId;
    private RequestMethod method;
    private String quickSearchParam;
    private Boolean optimistic;
    private Boolean submitForm;
    private ActionContextData actionContextData;
    private Integer size;
    private SubmitOn autoSubmitOn;
    private String datasourceId;

    @Getter
    @Setter
    public static class ActionContextData implements Serializable {
        private String objectId;
        private String operationId;
        private String route;
        private RedirectSaga redirect;
        private RefreshSaga refresh;
        private String parentWidgetId;
        private String failAlertWidgetId;
        private String successAlertWidgetId;
        private String messagesForm;
        private boolean messageOnSuccess;
        private boolean messageOnFail;
        private MessagePosition messagePosition;
        private MessagePlacement messagePlacement;
        private CompiledObject.Operation operation;
    }
}

