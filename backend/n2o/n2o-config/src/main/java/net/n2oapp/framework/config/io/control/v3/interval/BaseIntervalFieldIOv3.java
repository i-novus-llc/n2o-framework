package net.n2oapp.framework.config.io.control.v3.interval;

import net.n2oapp.framework.api.metadata.control.interval.N2oSimpleIntervalField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.v3.StandardFieldIOv3;
import org.jdom2.Element;

/**
 * Чтение, запись компоненета ввода интервала версии 3.0
 * @param <T>
 */
public abstract class BaseIntervalFieldIOv3<T extends N2oSimpleIntervalField> extends StandardFieldIOv3<T> {
    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        p.childAttribute(e, "default-value", "begin", m::getBegin, m::setBegin);
        p.childAttribute(e, "default-value", "end", m::getEnd, m::setEnd);
        p.attribute(e, "begin-param", m::getBeginParam, m::setBeginParam);
        p.attribute(e, "end-param", m::getEndParam, m::setEndParam);
    }
}
