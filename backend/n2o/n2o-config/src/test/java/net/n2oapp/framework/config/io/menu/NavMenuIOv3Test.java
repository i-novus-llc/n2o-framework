package net.n2oapp.framework.config.io.menu;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.action.AlertActionElementIOV2;
import net.n2oapp.framework.config.io.action.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.OpenPageElementIOV2;
import net.n2oapp.framework.config.test.XmlIOTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи основного меню 3.0
 */
class NavMenuIOv3Test extends XmlIOTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(
                new NavMenuIOv3(),
                new OpenPageElementIOV2(),
                new AnchorElementIOV2(),
                new AlertActionElementIOV2()
        );
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/menu/navMenuIOv3.menu.xml");
    }
}
