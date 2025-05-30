package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.PrintTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;

/**
 * Исходная модель действия печати
 */
@Getter
@Setter
public class N2oPrintAction extends N2oAbstractAction implements N2oAction {
    private String url;
    private PrintTypeEnum type;
    private Boolean keepIndent;
    private String documentTitle;
    private Boolean loader;
    private String loaderText;
    private Boolean base64;

    private N2oParam[] pathParams;
    private N2oParam[] queryParams;
}
