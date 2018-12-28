package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.config.reader.tools.PropertiesReaderV1;
import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.event.action.OnClick;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

/**
 * Считывает событие on-click
 */
@Component
public class OnClickReaderV1  extends AbstractN2oEventXmlReader<OnClick> {

    @Override
    public OnClick read(Element element) {
        OnClick res = new OnClick();
        return getOnClick(element, res, element.getNamespace());
    }

    public OnClick getOnClick(Element element, OnClick res, Namespace namespace) {
        if (element == null) return null;
        res.setSourceSrc(ReaderJdomUtil.getAttributeString(element, "src"));
        res.setFunctionName(ReaderJdomUtil.getAttributeString(element, "function-name"));
        res.setProperties(PropertiesReaderV1.getInstance().read(element, namespace));
        res.setNamespaceUri(element.getNamespaceURI());
        return res;
    }

    @Override
    public Class<OnClick> getElementClass() {
        return OnClick.class;
    }

    @Override
    public String getElementName() {
        return "on-click";
    }
}
