package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.control.SubmitOnEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.saga.LoadingSaga;
import net.n2oapp.framework.api.metadata.meta.saga.PollingSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePositionEnum;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethodEnum;

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
    private ReduxModelEnum targetModel;
    private String clientDatasourceId;
    private String datasourceId;
    private RequestMethodEnum method;
    private String quickSearchParam;
    private Boolean optimistic;
    private Boolean submitForm;
    private ActionContextData actionContextData;
    private Integer size;
    private SubmitOnEnum autoSubmitOn;

    @Getter
    @Setter
    public static class ActionContextData implements Serializable {
        private CompiledObject.Operation operation;
        private String objectId;
        private String operationId;
        private String route;
        private String clearDatasource;
        private LoadingSaga loading;
        private PollingSaga polling;
        private RedirectSaga redirect;
        private RefreshSaga refresh;
        @Deprecated
        private String parentWidgetId;

        private boolean messageOnSuccess;
        private boolean messageOnFail;
        private boolean useFailOut;
        private String messagesForm;
        private MessagePositionEnum messagePosition;
        private MessagePlacementEnum messagePlacement;
    }
}

