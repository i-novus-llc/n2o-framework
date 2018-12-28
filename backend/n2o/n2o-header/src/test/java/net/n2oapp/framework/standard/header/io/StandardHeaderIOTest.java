package net.n2oapp.framework.standard.header.io;

import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import net.n2oapp.framework.standard.header.model.global.N2oStandardHeader;
import net.n2oapp.framework.standard.header.persisiter.StandardHeaderModulePersister;
import net.n2oapp.framework.standard.header.persisiter.StandardHeaderPersister;
import net.n2oapp.framework.standard.header.reader.StandardHeaderReader;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rgalina
 * @since 04.05.2016
 */
public class StandardHeaderIOTest {
    private ION2oMetadataTester tester;

    @Before
    public void setUp() throws Exception {
        SelectiveReader reader = new SelectiveStandardReader()
                .addReader(new StandardHeaderReader());

        StandardHeaderPersister standardHeaderPersister = new StandardHeaderPersister();
        standardHeaderPersister.setHeaderModulePersister(new StandardHeaderModulePersister());

        SelectivePersister persister = new SelectiveStandardPersister()
                .addPersister(standardHeaderPersister);

        tester = new ION2oMetadataTester()
                .addReader(reader)
                .addPersister(persister);


    }

    @Test
    public void testIOStandardHeader() {
        assert tester.check("net/n2oapp/framework/standard/header/io/standardHeaderIOTest.header.xml",
                (N2oStandardHeader standardHeader) -> {
                    assert standardHeader.getUserMenu().getSrc().equals("testSrc");
                    assert standardHeader.getUserMenu().getUsernameContext().equals("testUserNameContext");
                });

    }

}
