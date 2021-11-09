package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.N2oFileUpload;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.HashMap;
import java.util.Map;

/**
 * Чтение/запись базового компонента загрузки файлов
 */
public abstract class BaseFileUploadIOv2<T extends N2oFileUpload> extends StandardFieldIOv2<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.child(e, null, "default-value", m::getDefValue, m::setDefValue, HashMap::new, this::defaultValue);
        p.attributeBoolean(e, "multi", m::getMulti, m::setMulti);
        p.attributeBoolean(e, "ajax", m::getAjax, m::setAjax);
        p.attributeBoolean(e, "show-size", m::getShowSize, m::setShowSize);
        p.attribute(e, "upload-url", m::getUploadUrl, m::setUploadUrl);
        p.attribute(e, "delete-url", m::getDeleteUrl, m::setDeleteUrl);
        p.attribute(e, "value-field-id", m::getValueFieldId, m::setValueFieldId);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "message-field-id", m::getMessageFieldId, m::setMessageFieldId);
        p.attribute(e, "url-field-id", m::getUrlFieldId, m::setUrlFieldId);
        p.attribute(e, "request-param", m::getRequestParam, m::setRequestParam);
        p.attribute(e, "accept", m::getAccept, m::setAccept);
    }

    private void defaultValue(Element e, Map<String, String> map, IOProcessor p) {
        p.otherAttributes(e, Namespace.NO_NAMESPACE, map);
    }
}
