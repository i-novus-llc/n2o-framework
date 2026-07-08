package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.action.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.SetValueElementIOV2;
import net.n2oapp.framework.config.io.action.SubmitActionElementIOV2;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения, записи поля с кнопкой версии 3.0
 */
class N2oButtonFieldXmlIOv3Test {

    @Test
    void testButtonFieldXmlIO() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ButtonFieldIOv3(), new FormElementIOV5(), new AnchorElementIOV2(),
                new SetValueElementIOV2(), new SubmitActionElementIOV2());

        assert tester.check("net/n2oapp/framework/config/io/control/testButtonFieldReaderV3.widget.xml");
    }
}
