package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.widget.form.FormElementIOV5;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи компонента компонента ввода времени
 */
public class N2oTimePickerXmlIOv3Test extends N2oStandardControlReaderTestBase {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CustomFieldIOv3(), new FormElementIOV5(), new CustomControlIOv3(), new TimePickerIOv3());
        assert tester.check("net/n2oapp/framework/config/io/control/v3/testTimePickerV3.widget.xml");
    }
}