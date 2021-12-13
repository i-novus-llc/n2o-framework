package net.n2oapp.framework.config.io.region.v2;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v4.WidgetIOv4;
import org.jdom2.Element;

/**
 * Чтение\запись базового региона версии 2.0
 */
public abstract class BaseRegionIOv2<T extends N2oRegion> extends AbstractRegionIOv2<T> {

    @Override
    public void io(Element e, T r, IOProcessor p) {
        super.io(e, r, p);
        p.anyChildren(e, null, r::getContent, r::setContent, p.anyOf(SourceComponent.class),
                WidgetIOv4.NAMESPACE, RegionIOv2.NAMESPACE);
    }
}