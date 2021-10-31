package net.n2oapp.framework.config.io.page.v2;

import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения\записи страницы с единственным виджетом версии 2.0
 */
public class SimplePageXmlIOv2Test {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SimplePageElementIOv2(), new TableElementIOV4());

        assert tester.check("net/n2oapp/framework/config/io/page/testSimplePageIOv2.page.xml");
    }
}
