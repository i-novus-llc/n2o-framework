package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.action.N2oCustomAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Чтение/запись настраиваемого действия версии 2.0
 */
@Component
public class CustomActionElementIOV2 extends AbstractMetaActionElementIOV2<N2oCustomAction> {
    @Override
    public Class<N2oCustomAction> getElementClass() {
        return N2oCustomAction.class;
    }

    @Override
    public String getElementName() {
        return "action";
    }

    @Override
    public void io(Element e, N2oCustomAction a, IOProcessor p) {
        super.io(e, a, p);
        p.attribute(e, "type", a::getType, a::setType);
        p.child(e, null, "payload", a::getPayload, a::setPayload, HashMap::new, this::payload);
    }

    private void payload(Element e, Map<String, String> map, IOProcessor p) {
        p.otherAttributes(e, Namespace.NO_NAMESPACE, map);
    }
}
