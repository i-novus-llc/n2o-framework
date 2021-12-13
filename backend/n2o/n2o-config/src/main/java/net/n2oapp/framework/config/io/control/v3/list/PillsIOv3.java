package net.n2oapp.framework.config.io.control.v3.list;

import net.n2oapp.framework.api.metadata.control.list.MultiType;
import net.n2oapp.framework.api.metadata.control.list.N2oPills;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента таблетка версии 3.0
 */
@Component
public class PillsIOv3 extends ListFieldIOv3<N2oPills> {
    @Override
    public Class<N2oPills> getElementClass() {
        return N2oPills.class;
    }

    @Override
    public String getElementName() {
        return "pills";
    }

    @Override
    public void io(Element e, N2oPills m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "type", m::getType, m::setType, MultiType.class);
    }
}
