package net.n2oapp.framework.config.reader.fieldset;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

/**
 * @author V. Alexeev.
 */
public class FieldSetReaderUtil {

    public static N2oFieldSet[] getFieldSet(Element formElement, Namespace namespace,
                                            final ElementReaderFactory readerFactory) {
        List<Element> sequences = formElement.getChildren();
        return sequences.stream().filter(el -> el.getName().equals("field-set"))
                .map(el -> readerFactory.produce(el).read(el))
                .toArray(N2oFieldSet[]::new);
    }



    public static void getType(Element rootElement, String name, N2oFieldSet fieldSet) {
        String src = ReaderJdomUtil.getAttributeString(rootElement, "src");
        if (src != null) {
            fieldSet.setSrc(src);
        }
    }
}
