package net.n2oapp.framework.api.pack;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;

public interface ReadersBuilder<B> {
    B readers(NamespaceReader<? extends NamespaceUriAware>... readers);

    B ios(NamespaceIO<? extends NamespaceUriAware>... ios);
}
