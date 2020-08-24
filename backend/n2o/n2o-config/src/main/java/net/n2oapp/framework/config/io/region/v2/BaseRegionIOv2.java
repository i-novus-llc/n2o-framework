package net.n2oapp.framework.config.io.region.v2;

import net.n2oapp.framework.api.metadata.global.view.region.N2oAbstractRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.WidgetIOv4;
import org.jdom2.Element;

/**
 * Чтение\запись базового региона версии 2.0
 */
public abstract class BaseRegionIOv2<T extends N2oRegion> extends AbstractRegionIOv2<T> {

    @Override
    public void io(Element e, T r, IOProcessor p) {
        super.io(e, r, p);
        p.anyChildren(e, null, r::getWidgets, r::setWidgets, p.anyOf(N2oWidget.class)
                .ignore(getRegions()), WidgetIOv4.NAMESPACE);
        p.anyChildren(e, null, r::getRegions, r::setRegions, p.anyOf(N2oAbstractRegion.class)
                .ignore(getWidgets()), RegionIOv2.NAMESPACE);
    }
}