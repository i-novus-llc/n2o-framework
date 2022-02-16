package net.n2oapp.framework.config.io.event;

import net.n2oapp.framework.api.metadata.application.N2oStompEvent;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.v2.AlertActionElementIOV2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись STOMP-события
 */
@Component
public class StompEventIO extends AbstractEventIO<N2oStompEvent>{

    @Override
    public Class<N2oStompEvent> getElementClass() {
        return N2oStompEvent.class;
    }

    @Override
    public String getElementName() {
        return "stomp-event";
    }

    @Override
    public void io(Element e, N2oStompEvent m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "destination", m::getDestination, m::setDestination);
        p.child(e, null, "alert", m::getAlert, m::setAlert, new AlertActionElementIOV2());
    }
}
