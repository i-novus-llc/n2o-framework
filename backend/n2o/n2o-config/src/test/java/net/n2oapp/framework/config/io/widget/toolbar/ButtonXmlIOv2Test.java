package net.n2oapp.framework.config.io.widget.toolbar;

import net.n2oapp.framework.config.io.action.AnchorElementIOV2;
import net.n2oapp.framework.config.io.action.CopyActionElementIOV2;
import net.n2oapp.framework.config.io.action.SetValueElementIOV2;
import net.n2oapp.framework.config.io.toolbar.ButtonIOv2;
import net.n2oapp.framework.config.io.widget.TableElementIOV5;
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
