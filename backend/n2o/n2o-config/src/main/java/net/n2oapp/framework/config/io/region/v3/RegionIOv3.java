package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import org.jdom2.Namespace;

/**
 * Чтение\запись региона версии 3.0
 */
public interface RegionIOv3 extends NamespaceUriAware, BaseElementClassAware<N2oRegion> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/region-3.0");

    @Override
    default String getNamespaceUri() {
        return NAMESPACE.getURI();
    }

    @Override
    default Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    default Class<N2oRegion> getBaseElementClass() {
        return N2oRegion.class;
    }

}