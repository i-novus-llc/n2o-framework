package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.pack.PersistersBuilder;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;

/**
 * Базовый класс для тестирования чтения/записи xml метаданных
 */
public abstract class XmlIOTestBase {

    protected ION2oMetadataTester tester;

    public void setUp() throws Exception {
        SelectiveReader reader = new SelectiveReader();
        SelectivePersister persister = new SelectivePersister();
        configure(new XmlIOBuilder<Object>() {
            @Override
            public Object persisters(NamespacePersister<? extends NamespaceUriAware>... persisters) {
                return null;
            }

            @Override
            public Object readers(NamespaceReader<? extends NamespaceUriAware>... readers) {
                return reader.readers(readers);
            }

            @Override
            public Object ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
                return reader.ios(ios);
            }
        });
        configure(new XmlIOBuilder<Object>() {
            @Override
            public Object persisters(NamespacePersister<? extends NamespaceUriAware>... persisters) {
                return persister.persisters(persisters);
            }

            @Override
            public Object readers(NamespaceReader<? extends NamespaceUriAware>... readers) {
                return null;
            }

            @Override
            public Object ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
                return persister.ios(ios);
            }
        });
        tester = new ION2oMetadataTester(reader, persister);
    }

    protected abstract void configure(XmlIOBuilder<?> b);

    public void check(String path) {
        tester.check(path);
    }
}
