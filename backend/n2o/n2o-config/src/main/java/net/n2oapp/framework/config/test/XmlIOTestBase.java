package net.n2oapp.framework.config.test;

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
        configure(ios -> reader.ios(ios));
        configure(ios -> persister.ios(ios));
        tester = new ION2oMetadataTester(reader, persister);
    }

    protected abstract void configure(XmlIOBuilder<?> b);

    public void check(String path) {
        tester.check(path);
    }
}
