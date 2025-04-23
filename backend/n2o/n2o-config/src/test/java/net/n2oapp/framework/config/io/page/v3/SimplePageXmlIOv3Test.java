package net.n2oapp.framework.config.io.page.v3;

import net.n2oapp.framework.config.io.widget.v4.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения\записи страницы с единственным виджетом версии 3.0
 */
class SimplePageXmlIOv3Test {
    
    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new SimplePageElementIOv3(), new TableElementIOV4());

        assert tester.check("net/n2oapp/framework/config/io/page/v3/testSimplePageIOv3.page.xml");
    }
}
