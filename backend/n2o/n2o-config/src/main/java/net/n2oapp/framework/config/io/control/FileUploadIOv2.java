package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class FileUploadIOv2 extends StandardFieldIOv2<N2oFileUpload> {

    @Override
    public void io(Element e, N2oFileUpload m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "multi", m::getMulti, m::setMulti);
        p.attributeBoolean(e, "ajax", m::getAjax, m::setAjax);
        p.attributeBoolean(e, "show-size", m::getShowSize, m::setShowSize);
        p.attribute(e, "upload-url", m::getUploadUrl, m::setUploadUrl);
        p.attribute(e, "delete-url", m::getDeleteUrl, m::setDeleteUrl);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "message-field-id", m::getMessageFieldId, m::setMessageFieldId);
        p.attribute(e, "url-field-id", m::getUrlFieldId, m::setUrlFieldId);
    }

    @Override
    public Class<N2oFileUpload> getElementClass() {
        return N2oFileUpload.class;
    }

    @Override
    public String getElementName() {
        return "file-upload";
    }
}
