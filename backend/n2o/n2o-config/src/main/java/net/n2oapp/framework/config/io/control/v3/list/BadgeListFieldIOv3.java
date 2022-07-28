package net.n2oapp.framework.config.io.control.v3.list;

import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.badge.Position;
import org.jdom2.Element;

/**
 * Чтение, запись спискового поля, содержащего значок версии 3.0
 */
public abstract class BadgeListFieldIOv3<T extends N2oListField> extends ListFieldIOv3<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "badge-field-id", m::getBadgeFieldId, m::setBadgeFieldId);
        p.attribute(e, "badge-color-field-id", m::getBadgeColorFieldId, m::setBadgeColorFieldId);
        p.attribute(e, "badge-image-field-id", m::getBadgeImageFieldId, m::setBadgeImageFieldId);
        p.attributeEnum(e, "badge-position", m::getBadgePosition, m::setBadgePosition, Position.class);
        p.attributeEnum(e, "badge-image-position", m::getBadgeImagePosition, m::setBadgeImagePosition, Position.class);
        p.attributeEnum(e, "badge-shape", m::getBadgeShape, m::setBadgeShape, ShapeType.class);
        p.attributeEnum(e, "badge-image-shape", m::getBadgeImageShape, m::setBadgeImageShape, ShapeType.class);
    }
}
