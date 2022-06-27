package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель действия сохранения источника данных
 */
@Getter
@Setter
public class N2oSubmitAction extends N2oAbstractAction implements N2oAction {
    private String datasource;
}
