package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;


/**
 * Действие вызова операции
 */
@Getter
@Setter
public class N2oInvokeAction extends N2oAbstractMetaAction {
    private String operationId;
    private String objectId;
    private String route;
    private RequestMethod method;
    private Boolean submitAll;
    private Boolean optimistic;
    private Boolean clearOnSuccess;
    private Boolean messageOnSuccess;
    private Boolean messageOnFail;
    private MessagePosition messagePosition;
    private MessagePlacement messagePlacement;

    private N2oFormParam[] formParams;
    private N2oParam[] pathParams;
    private N2oParam[] headerParams;
}
