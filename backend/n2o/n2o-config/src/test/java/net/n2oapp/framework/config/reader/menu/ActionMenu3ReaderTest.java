package net.n2oapp.framework.config.reader.menu;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.page.GenerateType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCustomWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * IO test на action-menu в таблицах
 */
public class ActionMenu3ReaderTest {

    private SelectiveReader reader;

    @Before
    public void beforeTest() {
        reader = new SelectiveStandardReader()
                .addWidgetReaderV3().addEventsReader();
    }

    @Test
    public void testIOMenuAttributes() {
        N2oCustomWidget widget = reader.readByPath("net/n2oapp/framework/config/io/menu/widget3/ioActionMenuAttributes3.widget.xml");
        N2oToolbar[] toolbars = widget.getToolbars();
        assertThat(toolbars[0].getGenerate()[0], is(GenerateType.crud.name()));
        assertThat(toolbars.length, is(1));
        assertThat(toolbars[0].getItems().length, is(2));
        N2oButton mi1 = (N2oButton) toolbars[0].getItems()[0];
        N2oButton mi2 = (N2oButton) toolbars[0].getItems()[1];
        assertThat(mi1.getId(), is("mi1"));
        assertThat(mi1.getLabel(), is("test"));
        assertThat(mi1.getColor(), is("test"));
        assertThat(mi1.getModel(), is(ReduxModel.RESOLVE));
        assertThat(mi1.getIcon(), is("test"));
        assertThat(mi1.getType(), is(LabelType.textAndIcon));
        assertThat(mi1.getVisible(), is("true"));

        assertThat(mi2.getId(), is("mi2"));
        assertThat(mi2.getLabel(), nullValue());
        assertThat(mi2.getColor(), nullValue());
        assertThat(mi2.getModel(), is(ReduxModel.RESOLVE));
        assertThat(mi2.getIcon(), nullValue());
        assertThat(mi2.getType(), nullValue());
        assertThat(mi2.getVisible(), nullValue());
        assertThat(mi2.getValidate(), nullValue());
    }

     @Test
    public void testIOMenuGroup() {
        N2oCustomWidget widget = reader.readByPath("net/n2oapp/framework/config/io/menu/widget3/ioActionMenuGroup3.widget.xml");
        N2oToolbar[] toolbars = widget.getToolbars();
        assertThat(toolbars.length, is(3));
        ToolbarItem[] defaultItems = toolbars[0].getItems();
        assertThat(defaultItems.length, is(4));
        assertThat(((N2oButton) defaultItems[0]).getId(), is("mi11"));
        assertThat(((N2oButton) defaultItems[1]).getId(), is("mi31"));
        assertThat(((N2oButton) defaultItems[2]).getId(), is("mi32"));
        assertThat(((N2oButton) defaultItems[3]).getId(), is("51"));

        N2oToolbar groupToolbar = toolbars[1];
        assertThat(groupToolbar.getItems().length, is(1));
        assertThat(groupToolbar.getPlace(), is("caspian"));
        N2oGroup group1 = (N2oGroup) groupToolbar.getItems()[0];
        assertThat(((N2oButton) group1.getItems()[0]).getId(), is("mi21"));
        assertThat(((N2oButton) group1.getItems()[1]).getId(), is("mi22"));
        assertThat(toolbars[2].getItems().length, is(1));
        assertThat(toolbars[2].getPlace(), is("bottom-right"));
        assertThat(((N2oButton) ((N2oGroup) toolbars[2].getItems()[0]).getItems()[0]).getId(), is("41"));
    }

    @Test
    public void testIOMenuConditions() {
        N2oCustomWidget widget = reader.readByPath("net/n2oapp/framework/config/io/menu/widget3/ioActionMenuConditions3.widget.xml");
        N2oToolbar[] toolbars = widget.getToolbars();
        assertThat(toolbars.length, is(1));
        N2oButton mi1 = (N2oButton) toolbars[0].getItems()[0];
        N2oButton mi2 = (N2oButton) toolbars[0].getItems()[1];
        assertThat(mi1.getEnablingConditions().length, is(1));
        assertThat(mi1.getEnablingConditions()[0].getExpression(), is("test == 1"));
        assertThat(mi1.getEnablingConditions()[0].getTooltip(), is("test"));
        assertThat(mi1.getEnablingConditions()[0].getOn(), is("test"));
        assertThat(mi1.getVisibilityConditions().length, is(1));
        assertThat(mi1.getVisibilityConditions()[0].getExpression(), is("test == 1"));
        assertThat(mi1.getVisibilityConditions()[0].getOn(), is("test"));
        assertThat(mi2.getEnablingConditions().length, is(1));
        assertThat(mi2.getEnablingConditions()[0].getExpression(), is("test == 1"));
        assertThat(mi2.getEnablingConditions()[0].getTooltip(), nullValue());
        assertThat(mi2.getEnablingConditions()[0].getOn(), nullValue());
        assertThat(mi2.getVisibilityConditions().length, is(1));
        assertThat(mi2.getVisibilityConditions()[0].getExpression(), is("test == 1"));
        assertThat(mi2.getVisibilityConditions()[0].getOn(), nullValue());

    }

    @Test
    public void testIOSubMenu() {
        N2oCustomWidget widget = reader.readByPath("net/n2oapp/framework/config/io/menu/widget3/ioActionMenuSubMenuItems3.widget.xml");
        N2oToolbar[] toolbars = widget.getToolbars();
        assertThat(toolbars.length, is(1));
        N2oSubmenu submenu = (N2oSubmenu) toolbars[0].getItems()[0];
        N2oButton button = (N2oButton) toolbars[0].getItems()[1];
        assertThat(submenu.getId(), is("mi1"));
        assertThat(submenu.getMenuItems().length, is(2));
        assertThat(submenu.getMenuItems()[0].getId(), is("smi"));
        assertThat(submenu.getMenuItems()[1].getId(), is("smi2"));
        assertThat(submenu.getMenuItems()[0].getAction(), instanceOf(N2oAnchor.class));
        assertThat(submenu.getMenuItems()[1].getAction(), instanceOf(N2oAnchor.class));
        assertThat(button.getId(), is("mi2"));
        assertThat(button.getAction(), instanceOf(N2oAnchor.class));
    }
}