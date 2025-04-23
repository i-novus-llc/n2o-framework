package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.v2.SetValueElementIOV2;
import net.n2oapp.framework.config.io.action.v2.SubmitActionElementIOV2;
import net.n2oapp.framework.config.io.widget.v5.FormElementIOV5;
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

        assert tester.check("net/n2oapp/framework/config/io/control/v3/testButtonFieldReaderV3.widget.xml");
    }
}
