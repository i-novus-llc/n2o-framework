package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;

public interface Button extends DatasourceIdAware, ModelAware, ActionsAware, IdAware {
    
    String getLabel();

    Boolean getValidate();

    void setValidate(Boolean validate);

    String[] getValidateDatasourceIds();

    void setValidateDatasourceIds(String[] validateDatasourceIds);

    String getColor();

    void setColor(String color);

    String getTooltipPosition();

    void setTooltipPosition(String tooltipPosition);

    String getIcon();

    void setIcon(String icon);

    Boolean getRounded();

    void setRounded(Boolean rounded);
}
