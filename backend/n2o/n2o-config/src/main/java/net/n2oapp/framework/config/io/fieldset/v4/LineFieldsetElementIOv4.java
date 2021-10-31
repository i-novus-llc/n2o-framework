package net.n2oapp.framework.config.io.fieldset.v4;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись филдсета с горизонтальной линией.
 */
@Component
public class LineFieldsetElementIOv4 extends FieldsetElementIOv4<N2oLineFieldSet> {

    @Override
    public void io(Element e, N2oLineFieldSet fs, IOProcessor p) {
        super.io(e, fs, p);
        p.attributeBoolean(e, "collapsible", fs::getCollapsible, fs::setCollapsible);
        p.attributeBoolean(e, "has-separator", fs::getHasSeparator, fs::setHasSeparator);
        p.attributeBoolean(e, "expand", fs::getExpand, fs::setExpand);
    }

    @Override
    public String getElementName() {
        return "line";
    }

    @Override
    public Class<N2oLineFieldSet> getElementClass() {
        return N2oLineFieldSet.class;
    }
}
