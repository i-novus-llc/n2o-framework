package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oImageUpload;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента загрузки изображений
 */
@Component
public class ImageUploadIOv2 extends BaseFileUploadIOv2<N2oImageUpload> {

    @Override
    public void io(Element e, N2oImageUpload m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "list-type", m::getListType, m::setListType, ImageUpload.ListType.class);
        p.attributeBoolean(e, "lightbox", m::getLightbox, m::setLightbox);
    }

    @Override
    public Class<N2oImageUpload> getElementClass() {
        return N2oImageUpload.class;
    }

    @Override
    public String getElementName() {
        return "image-upload";
    }
}
