package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.XmlIOReader;

/**
 * Базовый класс для тестирования чтения/записи xml метаданных
 */
public abstract class XmlIOTestBase {

    protected ION2oMetadataTester tester;

    public void setUp() throws Exception {
        XmlIOReader reader = new XmlIOReader();
        configure(reader::ios);
        tester = new ION2oMetadataTester(reader);
    }

    protected abstract void configure(XmlIOBuilder<?> b);

    public void check(String path) {
        tester.check(path);
    }
}
