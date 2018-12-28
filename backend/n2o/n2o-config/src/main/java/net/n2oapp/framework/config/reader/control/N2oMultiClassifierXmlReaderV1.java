package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiClassifier;
import org.springframework.stereotype.Component;

@Component
public class N2oMultiClassifierXmlReaderV1 extends N2oStandardControlReaderV1<N2oMultiClassifier> {
    @Override
    public N2oMultiClassifier read(Element element, Namespace namespace) {
        N2oMultiClassifier multiClassifier = new N2oMultiClassifier();
        multiClassifier.setSearchAsYouType(ReaderJdomUtil.getAttributeBoolean(element, "search-as-you-type", "search-are-you-type"));
        return (N2oMultiClassifier) getQueryFieldDefinition(element, multiClassifier);
    }

    @Override
    public Class<N2oMultiClassifier> getElementClass() {
        return N2oMultiClassifier.class;
    }

    @Override
    public String getElementName() {
        return "multi-classifier";
    }
}
