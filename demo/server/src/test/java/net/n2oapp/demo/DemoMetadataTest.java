package net.n2oapp.demo;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.test.N2oTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тест валидности метаданных демо стенда
 */
public class DemoMetadataTest extends N2oTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder b) {
        super.configure(b);
        b.loaders(new XmlMetadataLoader(b.getEnvironment().getNamespaceReaderFactory()));
        b.packs(new N2oAllValidatorsPack(), new N2oAllDataPack(), new N2oAllPagesPack(), new N2oHeaderPack());
        b.scanners(new XmlInfoScanner());
        builder.scan();
    }

    @Test
    public void validate() {
        builder.getEnvironment().getMetadataRegister().find(i -> true).forEach(i -> {
            builder.read().validate().get(i.getId(), i.getBaseSourceClass());
        });
    }
}
