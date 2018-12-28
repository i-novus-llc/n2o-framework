package net.n2oapp.framework.access.metadata.schema.user;

import net.n2oapp.framework.access.metadata.schema.simple.SimpleAccessSchemaReaderV1;
import net.n2oapp.framework.api.metadata.persister.ElementPersister;
import net.n2oapp.framework.api.metadata.aware.PersisterFactoryAware;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Класс для преобразования entity N2oUserAccess в элемент доступа user
 * @author dednakov
 * @since 29.12.2016
 */
public class UserPersister implements ElementPersister<N2oUserAccess>, PersisterFactoryAware {

    private NamespacePersisterFactory persisterFactory;

    public UserPersister(NamespacePersisterFactory persisterFactory) {
        this.persisterFactory = persisterFactory;
    }

    @Override
    public Element persist(N2oUserAccess entity, Namespace namespace) {
        Element element = PersisterJdomUtil.setElement("user");
        PersisterJdomUtil.setAttribute(element, "id", entity.getId());
        PersisterJdomUtil.setChildren(element, null, null, entity.getAccessPoints()
                , persisterFactory, SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB);
        return element;
    }

    @Override
    public void setPersisterFactory(NamespacePersisterFactory persisterFactory) {
        this.persisterFactory = persisterFactory;
    }
}
