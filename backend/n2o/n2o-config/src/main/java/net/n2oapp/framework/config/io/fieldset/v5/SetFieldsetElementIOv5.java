package net.n2oapp.framework.config.io.fieldset.v5;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oSetFieldSet;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись простого филдсета версии 5.0
 */
@Component
public class SetFieldsetElementIOv5 extends FieldsetElementIOv5<N2oSetFieldSet> {

    @Override
    public String getElementName() {
        return "set";
    }

    @Override
    public Class<N2oSetFieldSet> getElementClass() {
        return N2oSetFieldSet.class;
    }
}
