package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.plain.N2oRangeField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ControlIOv2;
import net.n2oapp.framework.config.io.control.interval.BaseIntervalFieldIOv2;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

@Component
public class RangeFieldIOv2 extends BaseIntervalFieldIOv2<N2oRangeField> {

    private Namespace controlNamespace = ControlIOv2.NAMESPACE;

    @Override
    public void io(Element e, N2oRangeField m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChild(e, "begin", m::getBeginControl, m::setBeginControl, p.anyOf(N2oStandardField.class),
                controlNamespace);
        p.anyChild(e, "end", m::getEndControl, m::setEndControl, p.anyOf(N2oStandardField.class), controlNamespace);
    }

    @Override
    public Class<N2oRangeField> getElementClass() {
        return N2oRangeField.class;
    }

    @Override
    public String getElementName() {
        return "interval-field";
    }
}
