package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;

/**
 * Событие очистки модели
 */
@Getter
@Setter
public class N2oClearAction extends N2oAbstractAction implements N2oAction, DatasourceIdAware {
    private String datasource;
    private String[] model;
    private Boolean closeOnSuccess;
}
