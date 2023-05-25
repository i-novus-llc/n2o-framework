package net.n2oapp.framework.config.io.custom.prefix;

import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.page.v4.StandardPageElementIOv4;
import net.n2oapp.framework.config.io.region.v3.PanelRegionIOv3;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.test.XmlIOTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения, записи компонента с указанным префиксом
 */
public class IOwithPrefixTest extends XmlIOTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(XmlIOBuilder<?> b) {
        b.ios(new StandardPageElementIOv4(), new PrefixElementIO(), new PanelRegionIOv3(), new FormElementIOV5());
    }

    @Test
    void test() {
        check("net/n2oapp/framework/config/testPageWithPrefix.page.xml");
    }
}