package net.n2oapp.framework.config.metadata.header;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderIOv2;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

public class SimpleHeaderIOv2Test extends XmlIOTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new SimpleHeaderIOv2(), new SimpleMenuIOv2());
    }

    @Test
    public void test() {
        check("net/n2oapp/framework/config/metadata/header/headerIOv2.header.xml");
    }
}
