package net.n2oapp.framework.config.metadata.menu;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование чтения/записи меню 1.0
 */
public class SimpleMenuIOv2Test extends XmlIOTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new SimpleMenuIOv2());
    }

    @Test
    public void test() {
        check("net/n2oapp/framework/config/metadata/menu/menuIOv2.menu.xml");
    }
}
