package net.n2oapp.framework.config.io.control;

import net.n2oapp.framework.config.io.action.ShowModalElementIOV2;
import net.n2oapp.framework.config.io.control.plain.InputTextIOv3;
import net.n2oapp.framework.config.io.dataprovider.SqlDataProviderIOv1;
import net.n2oapp.framework.config.io.toolbar.ButtonIOv2;
import net.n2oapp.framework.config.io.widget.FormElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи стандартного поля
 */
class N2oStandardFieldXmlIOv3Test {
    
    @Test
    void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new InputTextIOv3(), new FormElementIOV5(), new ButtonIOv2(),
                new ShowModalElementIOV2(), new SqlDataProviderIOv1());
        assert tester.check("net/n2oapp/framework/config/io/control/testStandardFieldV3.widget.xml");
    }
}
