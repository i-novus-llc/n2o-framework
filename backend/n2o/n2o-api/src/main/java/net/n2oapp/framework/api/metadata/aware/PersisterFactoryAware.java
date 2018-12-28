package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.persister.ElementPersisterFactory;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;

/**
 * Знание о фабрике персистеров
 */
public interface PersisterFactoryAware<T extends NamespaceUriAware, P extends NamespacePersister<? super T>> {

    void setPersisterFactory(NamespacePersisterFactory<T,P> persisterFactory);
}
