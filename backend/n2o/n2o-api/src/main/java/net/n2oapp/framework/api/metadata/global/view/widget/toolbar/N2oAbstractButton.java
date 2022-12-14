package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Абстрактная модель пункта меню.
 */
@Getter
@Setter
public abstract class N2oAbstractButton extends N2oComponent implements IdAware, BadgeAware {
    @N2oAttribute
    private String id;
    @N2oAttribute
    private String label;
    @N2oAttribute
    private String icon;
    @N2oAttribute
    private LabelType type;
    @N2oAttribute
    private String badge;
    @N2oAttribute
    private String badgeColor;
    @N2oAttribute
    private Position badgePosition;
    @N2oAttribute
    private ShapeType badgeShape;
    @N2oAttribute
    private String badgeImage;
    @N2oAttribute
    private Position badgeImagePosition;
    @N2oAttribute
    private ShapeType badgeImageShape;
    @N2oAttribute
    private String color;
    @N2oAttribute
    private String description;
    @N2oAttribute
    private String tooltipPosition;
    private ReduxModel model;
    private String datasourceId;
    @N2oAttribute
    private String visible;
    @N2oAttribute
    private String enabled;

    @Deprecated
    public String getWidgetId() {
        return datasourceId;
    }

    @Deprecated
    public void setWidgetId(String widgetId) {
        this.datasourceId = widgetId;
    }
}
