package net.n2oapp.framework.access.metadata.schema.permission;

import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaReaderV1;
import net.n2oapp.framework.api.metadata.persister.ElementPersister;
import net.n2oapp.framework.api.metadata.persister.ElementPersisterFactory;
import net.n2oapp.framework.api.metadata.aware.PersisterFactoryAware;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author V. Alexeev.
 */
public class PermissionPersister implements ElementPersister<N2oPermission>, PersisterFactoryAware {

    private NamespacePersisterFactory persisterFactory;

    public PermissionPersister(NamespacePersisterFactory persisterFactory) {
        this.persisterFactory = persisterFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Element persist(N2oPermission entity, Namespace namespace) {
        Element element = new Element("permission");
        PersisterJdomUtil.setAttribute(element, "id", entity.getId());
        PersisterJdomUtil.setAttribute(element, "name", entity.getName());
        PersisterJdomUtil.setChildren(element, null, null, entity.getAccessPoints()
                , persisterFactory, SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB);
        return element;
    }

    @Override
    public void setPersisterFactory(NamespacePersisterFactory persisterFactory) {
        this.persisterFactory = persisterFactory;
    }
}
