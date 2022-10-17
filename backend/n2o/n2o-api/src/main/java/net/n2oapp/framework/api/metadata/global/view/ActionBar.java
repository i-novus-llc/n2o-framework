package net.n2oapp.framework.api.metadata.global.view;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.WidgetIdAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;

/**
 * "Исходная" модель действия для ссылки в action-id (метадействие)
 */
@Getter
@Setter
public class ActionBar implements Source, IdAware, ModelAware, WidgetIdAware, DatasourceIdAware {
    private String id;
    private String label;
    private String icon;
    private String visible;
    private String enabled;
    private Boolean defaultValue;
    private N2oAction[] n2oActions;
    private ReduxModel model;
    private String datasourceId;

    public String getWidgetId() {
        return datasourceId;
    }

    public void setWidgetId(String widgetId) {
        this.datasourceId = widgetId;
    }
}
