package net.n2oapp.framework.config.io.menu.v3;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.v2.OpenPageElementIOV2;
import net.n2oapp.framework.config.io.menu.ExtraMenuIOv3;
import net.n2oapp.framework.config.test.XmlIOTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи дополнительного меню 3.0
 */
public class ExtraMenuIOv3Test extends XmlIOTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new ExtraMenuIOv3(), new OpenPageElementIOV2(), new AnchorElementIOV2());
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/io/menu/v3/extraMenuIOv3.menu.xml");
    }
}
