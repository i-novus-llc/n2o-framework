package net.n2oapp.framework.config.test;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.XmlIOReader;

/**
 * Базовый класс для тестирования чтения/записи xml метаданных
 */
public abstract class XmlIOTestBase {

    protected ION2oMetadataTester tester;

    public void setUp() {
        XmlIOReader reader = new XmlIOReader(new N2oEnvironment());
        configure(reader);
        tester = new ION2oMetadataTester(reader);
    }

    protected abstract void configure(XmlIOBuilder<?> b);

    public void check(String path) {
        tester.check(path);
    }
}
