package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

/**
 * Исходная модель элемента меню {@code <menu-item>}
 */
@Getter
@Setter
public class N2oMenuItem extends N2oAbstractMenuItem implements BadgeAware, ActionsAware {
    private String actionId;
    private N2oAction[] actions;
    private String badge;
    private String badgeColor;
    private PositionEnum badgePosition;
    private ShapeTypeEnum badgeShape;
    private String badgeImage;
    private PositionEnum badgeImagePosition;
    private ShapeTypeEnum badgeImageShape;
}