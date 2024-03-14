package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Абстрактная модель пункта меню.
 */
@Getter
@Setter
public abstract class N2oAbstractButton extends N2oComponent implements GroupItem, IdAware, BadgeAware, DatasourceIdAware {

    private String id;
    private String label;
    private String icon;
    private Position iconPosition;
    private String badge;
    private String badgeColor;
    private Position badgePosition;
    private ShapeType badgeShape;
    private String badgeImage;
    private Position badgeImagePosition;
    private ShapeType badgeImageShape;
    private String color;
    private String description;
    private String tooltipPosition;
    private ReduxModel model;
    private String datasourceId;
    private String visible;
    private String enabled;
    private String[] generate;

    @Deprecated
    public String getWidgetId() {
        return datasourceId;
    }

    @Deprecated
    public void setWidgetId(String widgetId) {
        this.datasourceId = widgetId;
    }
}
