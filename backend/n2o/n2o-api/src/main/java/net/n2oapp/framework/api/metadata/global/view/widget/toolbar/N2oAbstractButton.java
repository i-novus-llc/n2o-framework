package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.VisualAttribute;
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
    @VisualAttribute
    private String id;
    @VisualAttribute
    private String label;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private LabelType type;
    @VisualAttribute
    private String badge;
    @VisualAttribute
    private String badgeColor;
    @VisualAttribute
    private Position badgePosition;
    @VisualAttribute
    private ShapeType badgeShape;
    @VisualAttribute
    private String badgeImage;
    @VisualAttribute
    private Position badgeImagePosition;
    @VisualAttribute
    private ShapeType badgeImageShape;
    @VisualAttribute
    private String color;
    @VisualAttribute
    private String description;
    @VisualAttribute
    private String tooltipPosition;
    private ReduxModel model;
    private String datasourceId;
    @VisualAttribute
    private String visible;
    @VisualAttribute
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
