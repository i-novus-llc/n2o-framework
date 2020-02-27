package net.n2oapp.framework.config.io.widget.toolbar;

import net.n2oapp.framework.config.io.action.CopyActionElementIOV1;
import net.n2oapp.framework.config.io.toolbar.MenuItemIO;
import net.n2oapp.framework.config.io.toolbar.SubmenuIO;
import net.n2oapp.framework.config.io.widget.table.TableElementIOV4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тест на чтение/запись элемента <menu-item>
 */
public class MenuItemXmlIOTest {
    @Test
    public void testButton() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .ios(new TableElementIOV4(), new SubmenuIO(), new MenuItemIO(), new CopyActionElementIOV1());
        assert tester.check("net/n2oapp/framework/config/io/widget/toolbar/testMenuItemIO.widget.xml");
    }
}