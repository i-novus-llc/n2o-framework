package net.n2oapp.framework.access.metadata.schema;

import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
/**
 * @author V. Alexeev.
 */
@SuppressWarnings("unchecked")
public abstract class AccessSchemaPersister<T extends N2oAccessSchema> extends AbstractN2oMetadataPersister<T> {

    @Override
    public void setNamespacePrefix(String prefix) {
        this.namespacePrefix = prefix;
    }

    @Override
    public void setNamespaceUri(String uri) {
        this.namespaceUri = uri;
    }

    protected void persistAbstractAccessSchema(Element element, N2oAccessSchema schema) {
        PersisterJdomUtil.setAttribute(element, "id", schema.getId());
    }

}
