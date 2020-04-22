package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
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
    private ReduxModel model;
    private String targetWidgetId;
    private RequestMethod method;
    private String quickSearchParam;
    private Boolean optimistic;
    private Boolean submitForm;
}