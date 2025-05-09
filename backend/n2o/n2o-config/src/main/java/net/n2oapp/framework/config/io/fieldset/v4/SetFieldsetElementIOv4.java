package net.n2oapp.framework.config.io.fieldset.v4;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись простого филдсета.
 */
@Component
public class SetFieldsetElementIOv4 extends FieldsetElementIOv4<N2oSetFieldSet> {

    @Override
    public void io(Element e, N2oSetFieldSet fs, IOProcessor p) {
        super.io(e, fs, p);
        p.merge(fs, getElementName());
    }

    @Override
    public String getElementName() {
        return "set";
    }

    @Override
    public Class<N2oSetFieldSet> getElementClass() {
        return N2oSetFieldSet.class;
    }
}
