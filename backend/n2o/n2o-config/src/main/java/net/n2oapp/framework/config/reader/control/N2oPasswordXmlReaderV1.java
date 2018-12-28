package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oPassword;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */

@Component
public class N2oPasswordXmlReaderV1 extends N2oStandardControlReaderV1<N2oPassword> {

    @Override
    public String getElementName() {
        return "password";
    }

    @Override
    public N2oPassword read(Element element, Namespace namespace) {
        N2oPassword password = new N2oPassword();
        getControlFieldDefinition(element, password);
        password.setLength(ReaderJdomUtil.getAttributeInteger(element, "length"));
        return password;
    }

    @Override
    public Class<N2oPassword> getElementClass() {
        return N2oPassword.class;
    }
}
