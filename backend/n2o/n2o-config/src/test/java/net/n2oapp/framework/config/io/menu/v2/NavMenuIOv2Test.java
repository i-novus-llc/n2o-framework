package net.n2oapp.framework.config.io.menu.v2;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.menu.NavMenuIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи основного меню 2.0
 */
public class NavMenuIOv2Test extends XmlIOTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new NavMenuIOv2());
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/menu/v2/navMenuIOv2.menu.xml");
    }
}
