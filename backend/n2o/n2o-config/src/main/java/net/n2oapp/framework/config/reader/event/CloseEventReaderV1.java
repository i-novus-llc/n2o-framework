package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Reader для события <close>
 */
@Component
public class CloseEventReaderV1 extends AbstractN2oEventXmlReader<N2oCloseAction> {

    @Override
    public N2oCloseAction read(Element element) {
        if (element == null) return null;
        N2oCloseAction res = new N2oCloseAction();
        return res;
    }

    @Override
    public Class<N2oCloseAction> getElementClass() {
        return N2oCloseAction.class;
    }

    @Override
    public String getElementName() {
        return "close";
    }
}
