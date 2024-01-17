package net.n2oapp.framework.api.metadata.control.filter_buttons;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Абстрактная реализация кнопок фильтров
 */
@Getter
@Setter
public abstract class N2oFilterButtonField extends N2oField implements BadgeAware, DatasourceIdAware {

    private String icon;
    private String badge;
    private String badgeColor;
    private Position badgePosition;
    private ShapeType badgeShape;
    private String badgeImage;
    private Position badgeImagePosition;
    private ShapeType badgeImageShape;
    private String datasourceId;
    private ReduxModel model;
    private String color;
    private String tooltipPosition;
    private Boolean rounded;
}
