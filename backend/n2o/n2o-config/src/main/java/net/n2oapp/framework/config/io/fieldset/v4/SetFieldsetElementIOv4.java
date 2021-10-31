package net.n2oapp.framework.config.io.fieldset.v4;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись простого филдсета.
 */
@Component
public class SetFieldsetElementIOv4 extends FieldsetElementIOv4<N2oSetFieldSet> {

    @Override
    public String getElementName() {
        return "set";
    }

    @Override
    public Class<N2oSetFieldSet> getElementClass() {
        return N2oSetFieldSet.class;
    }
}
