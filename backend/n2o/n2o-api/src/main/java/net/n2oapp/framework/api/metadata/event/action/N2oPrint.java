package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;

/**
 * Исходная модель действия печати документа
 */
@Getter
@Setter
public class N2oPrint extends N2oAbstractAction implements N2oAction {
    private String url;
    private N2oParam[] pathParams;
    private N2oParam[] queryParams;
}
