package net.n2oapp.framework.config.io.widget.v5.toolbar;

import net.n2oapp.framework.config.io.action.v2.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.v2.CopyActionElementIOV2;
import net.n2oapp.framework.config.io.action.v2.SetValueElementIOV2;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOv2;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи кнопки
 */
class ButtonXmlIOv2Test {
    
    @Test
    void testButton() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .ios(new TableElementIOV5(), new ButtonIOv2(), new CopyActionElementIOV2(),
                        new SetValueElementIOV2(), new AnchorElementIOV2());
        assert tester.check("net/n2oapp/framework/config/io/widget/toolbar/testButtonIOv2.widget.xml");
    }
}
