package net.n2oapp.framework.config.metadata.menu;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuPersister;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuReader;
import org.junit.Test;

/**
 * Testing reading and persisting simple menu
 *
 * @author igafurov
 * @since 14.04.2017
 */
public class SimpleMenuXmlIOTest {

    @Test
    public void test() {
        ION2oMetadataTester tester = new ION2oMetadataTester()
                .addReader(new SimpleMenuReader())
                .addPersister(new SimpleMenuPersister() {
                });

        tester.check("net/n2oapp/framework/config/metadata/menu/simpleMenuIOTest.menu.xml", (N2oSimpleMenu menu) -> {

            assert menu.getSrc().equals("testSrc");
            assert menu.getMenuItems().length == 3;

            N2oSimpleMenu.MenuItem menuItem = menu.getMenuItems()[0];
            assert menuItem.getLabel().equals("test");
            assert menuItem.getIcon().equals("glyphicon glyphicon-star");
            assert menuItem.getPageId() == null;
            assert menuItem.getSubMenu().length == 2;

            N2oSimpleMenu.MenuItem subMenu = menuItem.getSubMenu()[0];
            assert subMenu.getLabel().equals("sublabel");
            assert subMenu.getPageId().equals("testPage");
            assert subMenu.getIcon().equals("glyphicon glyphicon-star");
            assert subMenu.getSubMenu() == null;

            subMenu = menuItem.getSubMenu()[1];
            assert subMenu.getLabel().equals("hrefLabel1");
            assert subMenu.getHref().equals("http://test.com");
            assert subMenu.getTarget().equals(Target.newWindow);

            menuItem = menu.getMenuItems()[1];
            assert menuItem.getLabel().equals("test2");
            assert menuItem.getPageId().equals("page2");
            assert menuItem.getIcon().equals("glyphicon glyphicon-star");
            assert menuItem.getSubMenu() == null;

            menuItem = menu.getMenuItems()[2];
            assert menuItem.getLabel().equals("hrefLabel2");
            assert menuItem.getPageId() == null;
            assert menuItem.getIcon().equals("test");
            assert menuItem.getSubMenu() == null;
            assert menuItem.getTarget().equals(Target.self);
        });
    }
}
