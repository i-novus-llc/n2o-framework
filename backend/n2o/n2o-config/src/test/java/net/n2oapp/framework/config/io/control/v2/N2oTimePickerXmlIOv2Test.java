package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.config.io.widget.form.FormElementIOV4;
import net.n2oapp.framework.config.reader.control.N2oStandardControlReaderTestBase;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирование чтения/записи компонента компонента ввода времени
 */
public class N2oTimePickerXmlIOv2Test extends N2oStandardControlReaderTestBase {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new CustomFieldIOv2(), new FormElementIOV4(), new CustomControlIOv2(), new TimePickerIOv2());
        assert tester.check("net/n2oapp/framework/config/io/control/v2/testTimePicker.widget.xml");
    }
}