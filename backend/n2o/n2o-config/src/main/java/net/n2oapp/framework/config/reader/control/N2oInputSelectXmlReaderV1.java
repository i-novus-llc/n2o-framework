package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.list.ListType;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

/**
 * @author V. Alexeev.
 */

@Component
public class N2oInputSelectXmlReaderV1 extends N2oStandardControlReaderV1<N2oInputSelect> {

    @Override
    public String getElementName() {
        return "input-select";
    }

    @Override
    public N2oInputSelect read(Element element, Namespace namespace) {
        N2oInputSelect inputSelect = new N2oInputSelect();
        inputSelect.setType(getAttributeEnum(element, "type", ListType.class));
        inputSelect.setResetOnBlur(!getAttributeBoolean(element, "store-on-input"));
        return getQueryFieldDefinition(element, inputSelect);
    }

    @Override
    public Class<N2oInputSelect> getElementClass() {
        return N2oInputSelect.class;
    }
}
