package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.N2oFormParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;

@Getter
@Setter
public class Submit implements Source {
    private String operationId;
    private Boolean messageOnSuccess;
    private Boolean messageOnFail;
    private String route;
    private N2oParam[] pathParams;
    private N2oParam[] headerParams;
    private N2oFormParam[] formParams;
    private Boolean refreshOnSuccess;
    private String refreshWidgetId;
}
