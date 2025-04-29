package net.n2oapp.framework.api.metadata.control.filter_buttons;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

/**
 * Абстрактная реализация кнопок фильтров
 */
@Getter
@Setter
public abstract class N2oFilterButtonField extends N2oField implements BadgeAware, DatasourceIdAware {

    private String icon;
    private String badge;
    private String badgeColor;
    private PositionEnum badgePosition;
    private ShapeTypeEnum badgeShape;
    private String badgeImage;
    private PositionEnum badgeImagePosition;
    private ShapeTypeEnum badgeImageShape;
    private String datasourceId;
    private ReduxModelEnum model;
    private String color;
    private String tooltipPosition;
    private Boolean rounded;
}
