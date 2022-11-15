package net.n2oapp.framework.config.io.action;

import net.n2oapp.framework.api.metadata.action.N2oAbstractAction;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись абстрактного действия
 */
@Component
public abstract class AbstractActionElementIOV1<T extends N2oAbstractAction>  implements NamespaceIO<T>, ActionIOv1 {


    @Override
    public void io(Element e, T a, IOProcessor p) {
        p.attribute(e,"src",a::getSrc, a::setSrc);
    }
}
