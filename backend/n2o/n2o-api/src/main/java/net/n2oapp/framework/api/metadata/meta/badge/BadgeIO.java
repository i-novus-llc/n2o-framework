package net.n2oapp.framework.api.metadata.meta.badge;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;

/**
 * Интерфейс чтения/записи компонентов {@link BadgeAware}
 */
public interface BadgeIO<T extends BadgeAware> {

    /**
     * Чтение/запись элемента, не использующего ссылки на поля
     */
    default void simpleIO(Element e, T m, IOProcessor p) {
        p.attribute(e, "badge", m::getBadge, m::setBadge);
        p.attribute(e, "badge-color", m::getBadgeColor, m::setBadgeColor);
        p.attribute(e, "badge-image", m::getBadgeImage, m::setBadgeImage);
        defaults(e, m, p);
    }

    /**
     * Чтение/запись элемента, ссылающегося на поля
     */
    default void referringIO(Element e, T m, IOProcessor p) {
        p.attribute(e, "badge-field-id", m::getBadgeFieldId, m::setBadgeFieldId);
        p.attribute(e, "badge-color-field-id", m::getBadgeColorFieldId, m::setBadgeColorFieldId);
        p.attribute(e, "badge-image-field-id", m::getBadgeImageFieldId, m::setBadgeImageFieldId);
        defaults(e, m, p);
    }

    /**
     * Чтение/запись общих свойств
     */
    private void defaults(Element e, T m, IOProcessor p) {
        p.attributeEnum(e, "badge-position", m::getBadgePosition, m::setBadgePosition, Position.class);
        p.attributeEnum(e, "badge-image-position", m::getBadgeImagePosition, m::setBadgeImagePosition, Position.class);
        p.attributeEnum(e, "badge-shape", m::getBadgeShape, m::setBadgeShape, ShapeType.class);
        p.attributeEnum(e, "badge-image-shape", m::getBadgeImageShape, m::setBadgeImageShape, ShapeType.class);
    }
}
