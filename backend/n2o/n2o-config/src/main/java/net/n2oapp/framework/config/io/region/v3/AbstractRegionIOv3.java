package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.control.ComponentIO;
import org.jdom2.Element;

/**
 * Чтение\запись абстрактного региона версии 3.0
 */
public abstract class AbstractRegionIOv3<T extends N2oRegion & NamespaceUriAware> extends ComponentIO<T> {

    @Override
    public void io(Element e, T r, IOProcessor p) {
        super.io(e, r, p);
        p.attribute(e, "id", r::getId, r::setId);
    }

    @Override
    public String getNamespaceUri() {
        return RegionIOv3.NAMESPACE.getURI();
    }
}
