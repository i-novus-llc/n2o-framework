package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oImageField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение запись поля Image с заголовком и подзаголовком
 */
@Component
public class ImageFieldIOv2 extends FieldIOv2<N2oImageField> {

    @Override
    public void io(Element e, N2oImageField m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "url", m::getUrl, m::setUrl);
        p.attribute(e, "data", m::getData, m::setData);
        p.attribute(e, "title", m::getTitle, m::setTitle);
        p.attribute(e, "description", m::getDescription, m::setDescription);
        p.attributeEnum(e, "text-position", m::getTextPosition, m::setTextPosition, TextPosition.class);
        p.attribute(e, "width", m::getWidth, m::setWidth);
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
