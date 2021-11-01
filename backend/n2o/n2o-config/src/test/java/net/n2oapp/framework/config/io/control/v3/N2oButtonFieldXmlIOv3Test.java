package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.widget.form.FormElementIOV5;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения, записи поля с кнопкой версии 3.0
 */
public class N2oButtonFieldXmlIOv3Test extends N2oStandardControlReaderTestBase {

    @Test
    public void testButtonFieldXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ButtonFieldIOv3(), new FormElementIOV5(), new AnchorElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/control/v3/testButtonFieldReaderV3.widget.xml");
    }
}
