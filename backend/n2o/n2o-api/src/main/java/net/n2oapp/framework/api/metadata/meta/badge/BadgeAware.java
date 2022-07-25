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

    default String getBadgeImageFieldId() {
        return null;
    }

    default String getBadgeFieldId() {
        return null;
    }

    default String getBadgeColorFieldId() {
        return null;
    }

    default String getBadgeImage() {
        return null;
    }

    default String getBadge() {
        return null;
    }

    default String getBadgeColor() {
        return null;
    }
}
