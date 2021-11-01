package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.action.ShowModalElementIOV1;
import net.n2oapp.framework.config.io.control.v2.plain.InputTextIOv2;
import net.n2oapp.framework.config.io.dataprovider.SqlDataProviderIOv1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи стандартного поля
 */
public class N2oStandardFieldXmlIOv2Test extends N2oStandardControlReaderTestBase {
    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new InputTextIOv2(), new FormElementIOV4(), new ButtonIO(),
                new ShowModalElementIOV1(), new SqlDataProviderIOv1());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testStandardField.widget.xml");
    }
}
