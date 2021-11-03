package net.n2oapp.framework.config.io.action.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись абстрактного действия версии 2.0
 */
@Component
public abstract class AbstractActionElementIOV2<T extends N2oAbstractAction>  implements NamespaceIO<T>, ActionIOv2 {

    @Override
    public void io(Element e, T a, IOProcessor p) {
        p.attribute(e,"src",a::getSrc, a::setSrc);
    }
}
