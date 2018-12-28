package net.n2oapp.framework.config.reader.event;

import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * User: iryabov
 * Date: 03.07.13
 * Time: 13:22
 */
@Component
public class InvokeActionReaderV1 extends AbstractN2oEventXmlReader<N2oInvokeAction> {
    private static final InvokeActionReaderV1 instance = new InvokeActionReaderV1();

    public  InvokeActionReaderV1() {}

    public static InvokeActionReaderV1 getInstance() {
        return instance;
    }

    public static N2oInvokeAction getInvokeEvent(Element fieldSetElement, Namespace namespace) {
        Element invokeActionElement = fieldSetElement.getChild("invoke-action", namespace);
        if (invokeActionElement != null) {
            return getInstance().read(invokeActionElement);
        }
        return null;
    }

    @Override
    public N2oInvokeAction read(Element element) {
        N2oInvokeAction invokeEvent = new N2oInvokeAction();
        invokeEvent.setOperationId(ReaderJdomUtil.getAttributeString(element, "action-id"));
        invokeEvent.setNamespaceUri(getNamespaceUri());
        return invokeEvent;
    }

    @Override
    public Class<N2oInvokeAction> getElementClass() {
        return N2oInvokeAction.class;
    }

    @Override
    public String getElementName() {
        return "invoke-action";
    }
}
