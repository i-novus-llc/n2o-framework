package net.n2oapp.framework.config.reader.header;

import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.header.N2oCustomHeader;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.config.reader.tools.PropertiesReaderV1;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

/**
 * Считывание переопредленного хидера
 */
@Component
public class CustomHeaderReader extends AbstractFactoredReader<N2oCustomHeader> {


    @Override
    public N2oCustomHeader read(Element element, Namespace namespace) {
        N2oCustomHeader res = new N2oCustomHeader();
        res.setSrc(ReaderJdomUtil.getElementString(element, "src"));
        res.setProperties(PropertiesReaderV1.getInstance().readFromPropertiesElement(element, namespace));
        return res;
    }


    @Override
    public Class<N2oCustomHeader> getElementClass() {
        return N2oCustomHeader.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/custom-header-1.0";
    }

    @Override
    public String getElementName() {
        return "header";
    }
}
