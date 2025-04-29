package net.n2oapp.framework.api.metadata.meta.badge;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;

/**
 * Наличие значка у компонента
 */
public interface BadgeAware {

    PositionEnum getBadgePosition();

    void setBadgePosition(PositionEnum badgePosition);

    ShapeTypeEnum getBadgeShape();

    void setBadgeShape(ShapeTypeEnum badgeShape);

    PositionEnum getBadgeImagePosition();

    void setBadgeImagePosition(PositionEnum imagePosition);

    ShapeTypeEnum getBadgeImageShape();

    void setBadgeImageShape(ShapeTypeEnum badgeImageShape);

    default String getBadgeImageFieldId() {
        return null;
    }

    default void setBadgeImageFieldId(String imageFieldId){
    }

    default String getBadgeFieldId() {
        return null;
    }

    default void setBadgeFieldId(String badgeFieldId) {
    }

    default String getBadgeColorFieldId() {
        return null;
    }

    default void setBadgeColorFieldId(String badgeColorFieldId) {
    }

    default String getBadgeImage() {
        return null;
    }

    default void setBadgeImage(String badgeImage) {
    }

    default String getBadge() {
        return null;
    }

    default void setBadge(String badge) {
    }

    default String getBadgeColor() {
        return null;
    }

    default void setBadgeColor(String badgeColor) {
    }
}
