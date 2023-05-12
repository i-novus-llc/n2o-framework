package net.n2oapp.framework.config.io.widget.v4.toolbar;

import net.n2oapp.framework.config.io.action.CopyActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.ButtonIO;
import net.n2oapp.framework.config.io.widget.v4.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирование чтения/записи кнопки
 */
public class ButtonXmlIOTest {
    
    @Test
    void testButton() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .ios(new TableElementIOV4(), new ButtonIO(), new CopyActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/widget/toolbar/testButtonIO.widget.xml");
    }
}
