package net.n2oapp.framework.config.io.action.v1;

import net.n2oapp.framework.config.io.action.RefreshActionElementIOV1;
import net.n2oapp.framework.config.io.page.v3.StandardPageElementIOv3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи действия обновления данных виджета
 */
public class RefreshActionElementIOV1Test {
    @Test
    public void testRefreshActionElementIOV1Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new StandardPageElementIOv3(), new RefreshActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/action/v1/testRefreshActionElementIOV1.page.xml");
    }
}
