package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.global.view.region.AlignEnum;
import net.n2oapp.framework.api.metadata.global.view.region.JustifyEnum;
import net.n2oapp.framework.api.metadata.global.view.region.N2oFlexRowRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись региона {@code <flex-row>} версии 3.0
 */
@Component
public class FlexRowRegionIOv3 extends BaseRegionIOv3<N2oFlexRowRegion> {

    @Override
    public void io(Element e, N2oFlexRowRegion r, IOProcessor p) {
        super.io(e, r, p);
        p.attributeBoolean(e, "wrap", r::getWrap, r::setWrap);
        p.attributeEnum(e, "align", r::getAlign, r::setAlign, AlignEnum.class);
        p.attributeEnum(e, "justify", r::getJustify, r::setJustify, JustifyEnum.class);
    }

    @Override
    public Class<N2oFlexRowRegion> getElementClass() {
        return N2oFlexRowRegion.class;
    }

    @Override
    public String getElementName() {
        return "flex-row";
    }
}