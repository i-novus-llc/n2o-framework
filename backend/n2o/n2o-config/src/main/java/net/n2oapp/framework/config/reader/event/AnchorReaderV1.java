package net.n2oapp.framework.config.reader.event;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

/**
 * Считывает эвент ссылка из xml-файла в source-модель n2o
 */
@Component
public class AnchorReaderV1 extends AbstractN2oEventXmlReader<N2oAnchor> {

    @Override
    public N2oAnchor read(Element element) {
        if (element == null) return null;
        N2oAnchor res = new N2oAnchor(ReaderJdomUtil.getAttributeString(element, "href"));
        res.setTarget(ReaderJdomUtil.getAttributeEnum(element, "target", Target.class));
        res.setNamespaceUri(element.getNamespaceURI());
        return res;
    }

    @Override
    public Class<N2oAnchor> getElementClass() {
        return N2oAnchor.class;
    }

    @Override
    public String getElementName() {
        return "a";
    }
}
