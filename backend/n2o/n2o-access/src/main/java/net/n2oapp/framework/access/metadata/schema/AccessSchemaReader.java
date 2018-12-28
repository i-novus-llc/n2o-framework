package net.n2oapp.framework.access.metadata.schema;

import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author V. Alexeev.
 */
@SuppressWarnings("unchecked")
public abstract class AccessSchemaReader<T extends N2oAccessSchema> extends AbstractFactoredReader<T> {

    protected void getAbstractAccessSchemaDefinition(Element element, Namespace namespace, N2oAccessSchema access) {
        access.setId(ReaderJdomUtil.getAttributeString(element, "id"));
    }

}
