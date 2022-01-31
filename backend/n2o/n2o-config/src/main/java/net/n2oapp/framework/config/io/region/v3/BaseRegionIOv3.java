package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;

/**
 * Чтение\запись базового региона версии 3.0
 */
public abstract class BaseRegionIOv3<T extends N2oRegion> extends AbstractRegionIOv3<T> {

    @Override
    public void io(Element e, T r, IOProcessor p) {
        super.io(e, r, p);
        p.anyChildren(e, null, r::getContent, r::setContent, p.anyOf(SourceComponent.class),
                WidgetIOv5.NAMESPACE, RegionIOv3.NAMESPACE);
    }
}