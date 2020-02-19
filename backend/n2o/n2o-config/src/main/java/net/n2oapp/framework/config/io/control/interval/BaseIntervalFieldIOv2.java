package net.n2oapp.framework.config.io.control.interval;

import net.n2oapp.framework.api.metadata.control.interval.N2oSimpleIntervalField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.StandardFieldIOv2;
import org.jdom.Element;

public abstract class BaseIntervalFieldIOv2<T extends N2oSimpleIntervalField> extends StandardFieldIOv2<T> {
    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.childAttribute(e, "default-value", "begin", m::getBegin, m::setBegin);
        p.childAttribute(e, "default-value", "end", m::getEnd, m::setEnd);
    }
}
