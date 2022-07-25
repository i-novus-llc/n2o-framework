package net.n2oapp.framework.api.metadata.meta.badge;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

/**
 * Наличие значка у компонента
 */
public interface BadgeAware {

    Position getBadgePosition();

    ShapeType getBadgeShape();

    Position getBadgeImagePosition();

    ShapeType getBadgeImageShape();

    String getBadgeImageFieldId();

    String getBadgeFieldId();

    String getBadgeColorFieldId();
}
