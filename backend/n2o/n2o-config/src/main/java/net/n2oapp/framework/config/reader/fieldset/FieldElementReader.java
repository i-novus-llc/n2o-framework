package net.n2oapp.framework.config.reader.fieldset;

import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import org.jdom.Element;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.api.metadata.control.N2oField;

/**
 * User: iryabov
 * Date: 18.10.13
 * Time: 15:25
 */
public class FieldElementReader implements TypedElementReader<N2oField> {
    private NamespaceReaderFactory extensionReaderFactory;

    public FieldElementReader(
            NamespaceReaderFactory extensionReaderFactory) {
        this.extensionReaderFactory = extensionReaderFactory;
    }

    @Override
    public N2oField read(Element element) {
        N2oField n2oField = (N2oField) extensionReaderFactory.produce(element).read(element);

        return n2oField;
    }

    @Override
    public Class<N2oField> getElementClass() {
        return N2oField.class;
    }

    @Override
    public String getElementName() {
        return "field";
    }
}
