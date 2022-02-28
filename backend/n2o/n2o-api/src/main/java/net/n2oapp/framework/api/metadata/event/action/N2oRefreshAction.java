package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;

/**
 * Исходная модель действия обновления данных виджета
 */
@Getter
@Setter
public class N2oRefreshAction extends N2oAbstractAction implements N2oAction, DatasourceIdAware {
    private String datasourceId;

    @Deprecated
    public String getWidgetId() {
        return datasourceId;
    }

    @Deprecated
    public void setWidgetId(String widgetId) {
        this.datasourceId = widgetId;
    }
}
