package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v4.WidgetIOv4;
import org.jdom2.Element;

/**
 * Чтение\запись базового региона версии 1.0
 */
public abstract class BaseRegionIOv1<T extends N2oRegion> extends AbstractRegionIOv1<T> {

    @Override
    public void io(Element e, T r, IOProcessor p) {
        super.io(e, r, p);
        p.anyChildren(e, null, r::getContent, r::setContent, p.anyOf(SourceComponent.class), WidgetIOv4.NAMESPACE);
    }
}