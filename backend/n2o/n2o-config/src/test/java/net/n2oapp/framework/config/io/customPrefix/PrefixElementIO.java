package net.n2oapp.framework.config.io.customPrefix;

import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class PrefixElementIO implements NamespaceIO<N2oTestModel> {

    @Override
    public void io(Element e, N2oTestModel fs, IOProcessor p) {
    }

    @Override
    public Class<N2oTestModel> getElementClass() {
        return N2oTestModel.class;
    }

    @Override
    public String getElementName() {
        return "set";
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/some-schema";
    }
}
