package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.api.metadata.aware.BaseElementClassAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import org.jdom.Namespace;

public interface RegionIOv1 extends NamespaceUriAware, BaseElementClassAware<N2oRegion> {
    Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/region-1.0");


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