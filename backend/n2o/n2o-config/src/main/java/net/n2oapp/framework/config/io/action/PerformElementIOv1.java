package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.action.N2oPerform;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись действия Perform
 */
@Component
public class PerformElementIOv1 extends AbstractActionElementIOV1<N2oPerform> {

    @Override
    public void io(Element e, N2oPerform op, IOProcessor p) {
        super.io(e, op, p);
        p.attribute(e, "type", op::getType, op::setType);
        p.anyAttributes(e, op::getExtAttributes, op::setExtAttributes);
    }

    @Override
    public String getElementName() {
        return "perform";
    }

    @Override
    public Class<N2oPerform> getElementClass() {
        return N2oPerform.class;
    }
}
