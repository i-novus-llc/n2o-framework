package net.n2oapp.framework.config.io.region.v2;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.control.ComponentIO;
import org.jdom2.Element;

/**
 * Чтение\запись абстрактного региона версии 2.0
 */
public abstract class AbstractRegionIOv2<T extends N2oRegion & NamespaceUriAware> extends ComponentIO<T> implements NamespaceIO<T> {

    @Override
    public void io(Element e, T r, IOProcessor p) {
        super.io(e, r, p);
        p.attribute(e, "id", r::getId, r::setId);
    }

    @Override
    public String getNamespaceUri() {
        return RegionIOv2.NAMESPACE.getURI();
    }
}
