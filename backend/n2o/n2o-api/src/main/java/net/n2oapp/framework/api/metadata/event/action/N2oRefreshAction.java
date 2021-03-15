package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель действия обновления данных виджета
 */
@Getter
@Setter
public class N2oRefreshAction extends N2oAbstractAction implements N2oAction {
    private String widgetId;
}
