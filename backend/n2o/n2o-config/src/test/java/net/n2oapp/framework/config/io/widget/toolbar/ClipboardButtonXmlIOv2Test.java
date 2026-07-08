package net.n2oapp.framework.config.io.widget.toolbar;

import net.n2oapp.framework.config.io.toolbar.ClipboardButtonIOv2;
import net.n2oapp.framework.config.io.widget.TableElementIOV5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи кнопки {@code <clipboard-button>}
 */
class ClipboardButtonXmlIOv2Test {
    
    @Test
    void testClipboardButton() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .ios(new TableElementIOV5(), new ClipboardButtonIOv2());
        assert tester.check("net/n2oapp/framework/config/io/widget/toolbar/testClipboardButtonIOv2.widget.xml");
    }
}
