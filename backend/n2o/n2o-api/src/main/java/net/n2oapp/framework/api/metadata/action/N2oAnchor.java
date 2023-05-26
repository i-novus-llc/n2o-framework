package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Исходная модель ccылки
 */
@Getter
@Setter
public class N2oAnchor extends N2oAbstractAction implements N2oAction {
    private String href;
    private String datasourceId;
    private ReduxModel model;
    private Target target;
    private N2oParam[] pathParams;
    private N2oParam[] queryParams;

    /**
     * флаг для восстановления фильтров, используется в CloseAction и всегда true, если не в модалке
     */
    private Boolean restore;

    public N2oAnchor() {
    }

    public N2oAnchor(String href) {
        this.href = href;
    }
}
