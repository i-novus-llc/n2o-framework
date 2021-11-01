package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи страницы с единственным виджетом версии 4.0
 */
public class SimplePageXmlIOv4Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SimplePageElementIOv4(), new TableElementIOV5());

        assert tester.check("net/n2oapp/framework/config/io/page/v4/testSimplePageIOv4.page.xml");
    }
}
