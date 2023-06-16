package net.n2oapp.framework.config.io.menu.v2;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.menu.ExtraMenuIOv2;
import net.n2oapp.framework.config.test.XmlIOTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи дополнительного меню 2.0
 */
public class ExtraMenuIOv2Test extends XmlIOTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new ExtraMenuIOv2());
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/menu/v2/extraMenuIOv2.menu.xml");
    }
}
