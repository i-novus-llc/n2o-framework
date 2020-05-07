package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;

/**
 *  Провайдер данных клиента
 */
@Getter
@Setter
public class N2oClientDataProvider implements Source {
    private String id;
    private String url;
    private N2oParam[] formParams;
    private N2oParam[] pathParams;
    private N2oParam[] headerParams;
    private N2oParam[] queryParams;
    private ReduxModel targetModel;
    private String targetWidgetId;
    private RequestMethod method;
    private String quickSearchParam;
    private Boolean optimistic;
    private Boolean submitForm;
    private ActionContextData actionContextData;

    @Getter
    @Setter
    public static class ActionContextData implements Source {
        private String objectId;
        private String operationId;
        private String route;
        private RedirectSaga redirect;
        private String parentWidgetId;
        private String failAlertWidgetId;
        private String successAlertWidgetId;
        private String messagesForm;
        private boolean messageOnSuccess;
        private boolean messageOnFail;
        private CompiledObject.Operation operation;
    }
}