package net.n2oapp.framework.api.pack;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;

public interface PersistersBuilder<B> {

    B persisters(NamespacePersister<? extends NamespaceUriAware>... persisters);

    B ios(NamespaceIO<? extends NamespaceUriAware>... ios);
}
