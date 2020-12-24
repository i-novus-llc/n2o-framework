package net.n2oapp.framework.config.io.region;

import net.n2oapp.framework.api.metadata.global.view.region.N2oLineRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона с горизонтальным делителем версии 1.0
 */
@Component
public class LineRegionIOv1 extends BaseRegionIOv1<N2oLineRegion> {
    @Override
    public void io(Element e, N2oLineRegion r, IOProcessor p) {
        super.io(e, r, p);
        // define region title by first widget name
        if (!e.getChildren().isEmpty())
            p.read(e.getChildren().get(0), r, (w, reg) -> reg.setLabel(
                    w.getAttribute("name") != null ? w.getAttribute("name").getValue() : null));
        p.attributeBoolean(e, "collapsible", r::getCollapsible, r::setCollapsible);
    }

    @Override
    public String getElementName() {
        return "line";
    }

    @Override
    public Class<N2oLineRegion> getElementClass() {
        return N2oLineRegion.class;
    }
}
