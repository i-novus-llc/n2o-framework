package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.widget.WidgetIOv4;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Чтение\запись региона  версии 1.0
 */
public abstract class BaseRegionIOv1<T extends N2oRegion & NamespaceUriAware> implements NamespaceIO<T> {
    private Namespace widgetDefaultNamespace = WidgetIOv4.NAMESPACE;

    @Override
    public void io(Element e, T r, IOProcessor p) {
        p.attribute(e, "id", r::getId, r::setId);
        p.attribute(e, "place", r::getPlace, r::setPlace);
        p.attribute(e, "src", r::getSrc, r::setSrc);
        p.attribute(e, "class", r::getClassName, r::setClassName);
        p.attribute(e, "style", r::getStyle, r::setStyle);
        p.anyChildren(e, null, r::getWidgets, r::setWidgets, p.anyOf(N2oWidget.class), widgetDefaultNamespace);
        p.anyAttributes(e, r::getExtAttributes, r::setExtAttributes);
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/region-1.0";
    }

    public void setDataProviderDefaultNamespace(String widgetDefaultNamespace) {
        this.widgetDefaultNamespace = Namespace.getNamespace(widgetDefaultNamespace);
    }


}

