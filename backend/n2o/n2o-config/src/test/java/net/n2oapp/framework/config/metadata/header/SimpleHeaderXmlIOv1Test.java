package net.n2oapp.framework.config.metadata.header;

import net.n2oapp.framework.api.pack.PersistersBuilder;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderPersister;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderReader;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тест чтения/записи простого хедера v1
 */
public class SimpleHeaderXmlIOv1Test extends XmlIOTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.readers(new SimpleHeaderReader());
        b.persisters(new SimpleHeaderPersister());
    }

    @Test
    public void test() {
        check("net/n2oapp/framework/config/metadata/header/simpleHeaderIOTest.header.xml");
    }
}
