package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oImageField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageStatusElement;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента вывода изображения
 */
@Component
public class ImageFieldIOv3 extends ActionFieldIOv3<N2oImageField> {

    @Override
    public void io(Element e, N2oImageField m, IOProcessor p) {
        super.io(e, m, p);
        action(e, m, p, "statuses");
        p.attribute(e, "url", m::getUrl, m::setUrl);
        p.attribute(e, "data", m::getData, m::setData);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attribute(e, "description", m::getDescription, m::setDescription);
        p.attributeEnum(e, "text-position", m::getTextPosition, m::setTextPosition, TextPosition.class);
        p.attributeEnum(e, "shape", m::getShape, m::setShape, ShapeType.class);
        p.attribute(e, "width", m::getWidth, m::setWidth);
        p.children(e, "statuses", "status", m::getStatuses, m::setStatuses, N2oImageStatusElement::new, this::statuses);
    }

    private void statuses(Element e, N2oImageStatusElement c, IOProcessor p) {
        p.attribute(e, "src", c::getSrc, c::setSrc);
        p.attribute(e, "field-id", c::getFieldId, c::setFieldId);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attributeEnum(e, "place", c::getPlace, c::setPlace, ImageStatusElementPlace.class);
    }

    @Override
    public String actionsSequenceTag() {
        return "actions";
    }

    @Override
    public Class<N2oImageField> getElementClass() {
        return N2oImageField.class;
    }

    @Override
    public String getElementName() {
        return "image";
    }
}
