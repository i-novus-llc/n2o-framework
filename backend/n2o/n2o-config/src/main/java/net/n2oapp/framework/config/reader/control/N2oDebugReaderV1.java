package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.N2oDebug;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author iryabov
 * @since 09.11.2016
 */
@Component
public class N2oDebugReaderV1 extends N2oStandardControlReaderV1<N2oDebug> {
    @Override
    public String getElementName() {
        return "debug";
    }

    @Override
    public Class<N2oDebug> getElementClass() {
        return N2oDebug.class;
    }

    @Override
    public N2oDebug read(Element element, Namespace namespace) {
        N2oDebug control = new N2oDebug();
        getControlFieldDefinition(element, control);
        return control;
    }
}
