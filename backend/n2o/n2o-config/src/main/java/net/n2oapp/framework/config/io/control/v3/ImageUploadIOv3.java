package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oImageUpload;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента загрузки изображений версии 3.0
 */
@Component
public class ImageUploadIOv3 extends BaseFileUploadIOv3<N2oImageUpload> {

    @Override
    public void io(Element e, N2oImageUpload m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "list-type", m::getListType, m::setListType, ImageUpload.ListType.class);
        p.attributeBoolean(e, "can-lightbox", m::getCanLightbox, m::setCanLightbox);
        p.attributeBoolean(e, "can-delete", m::getCanDelete, m::setCanDelete);
        p.attributeInteger(e, "width", m::getWidth, m::setWidth);
        p.attributeInteger(e, "height", m::getHeight, m::setHeight);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attributeInteger(e, "icon-size", m::getIconSize, m::setIconSize);
        p.attributeBoolean(e, "show-tooltip", m::getShowTooltip, m::setShowTooltip);
        p.attributeEnum(e, "shape", m::getShape, m::setShape, ImageShape.class);
        p.attributeBoolean(e, "show-name", m::getShowName, m::setShowName);
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
