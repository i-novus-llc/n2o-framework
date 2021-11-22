package net.n2oapp.framework.config.metadata.menu;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv3;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование чтения/записи меню 3.0
 */
public class SimpleMenuIOv3Test extends XmlIOTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new SimpleMenuIOv3());
    }

    @Test
    public void test() {
        check("net/n2oapp/framework/config/metadata/menu/menuIOv3.menu.xml");
    }
}
