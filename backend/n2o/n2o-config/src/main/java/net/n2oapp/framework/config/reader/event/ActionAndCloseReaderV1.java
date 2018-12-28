package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oActionAndClose;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Reader для события <invoke-action-and-close>
 */
@Component
public class ActionAndCloseReaderV1 extends AbstractN2oEventXmlReader<N2oActionAndClose> {

    @Override
    public N2oActionAndClose read(Element element) {
        if (element == null) return null;
        N2oActionAndClose res = new N2oActionAndClose();
        res.setOperationId(ReaderJdomUtil.getAttributeString(element, "action-id"));
        res.setConfirmation(ReaderJdomUtil.getAttributeBoolean(element, "confirmation"));
        return res;
    }

    @Override
    public Class<N2oActionAndClose> getElementClass() {
        return N2oActionAndClose.class;
    }

    @Override
    public String getElementName() {
        return "invoke-action-and-close";
    }
}
