package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2OValidateAction;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Reader для события <validate>
 */
@Component
public class ValidateEventReaderV1 extends AbstractN2oEventXmlReader<N2OValidateAction> {

    @Override
    public N2OValidateAction read(Element element) {
        if (element == null) return null;
        N2OValidateAction res = new N2OValidateAction();
        return res;
    }

    @Override
    public Class<N2OValidateAction> getElementClass() {
        return N2OValidateAction.class;
    }

    @Override
    public String getElementName() {
        return "validate";
    }
}
