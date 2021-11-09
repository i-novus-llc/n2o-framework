package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.action.v2.ShowModalElementIOV2;
import net.n2oapp.framework.config.io.control.v3.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.dataprovider.SqlDataProviderIOv1;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи стандартного поля
 */
public class N2oStandardFieldXmlIOv3Test extends N2oStandardControlReaderTestBase {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new InputTextIOv3(), new FormElementIOV5(), new ButtonIOv2(),
                new ShowModalElementIOV2(), new SqlDataProviderIOv1());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testStandardFieldV3.widget.xml");
    }
}
