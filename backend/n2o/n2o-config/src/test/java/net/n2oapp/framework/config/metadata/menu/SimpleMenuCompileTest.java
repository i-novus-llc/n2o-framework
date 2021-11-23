package net.n2oapp.framework.config.metadata.menu;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.BadgeCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationCompiler;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationIOv2;
import net.n2oapp.framework.config.metadata.compile.cell.BadgeCellCompiler;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuCompiler;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv3;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SimpleMenuCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
        builder.ios(new SimpleMenuIOv3(), new ApplicationIOv2());
        builder.compilers(new SimpleMenuCompiler(), new ApplicationCompiler());
    }

    @Test
    public void testMenuItem() {
        Application application = compile(
                "net/n2oapp/framework/config/metadata/menu/testApplication.application.xml")
                .get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getMenu();
        HeaderItem menuItem = menu.getItems().get(0);

        assertThat(menuItem.getId(), is("notif"));
        assertThat(menuItem.getTitle(), is("Уведомления"));
        assertThat(menuItem.getIcon(), is("fa fa-bell"));
        assertThat(menuItem.getBadge(), is(2));
        assertThat(menuItem.getBadgeColor(), is("warning"));
        checkAction(menuItem.getAction());
    }

    @Test
    public void testDropdownMenu() {
        Application application = compile(
                "net/n2oapp/framework/config/metadata/menu/testApplication.application.xml")
                .get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getMenu();
        HeaderItem dropdownMenu = menu.getItems().get(1);

        assertThat(dropdownMenu.getId(), is("user"));
        assertThat(dropdownMenu.getTitle(), is("Виктория"));
        assertThat(dropdownMenu.getImageSrc(), is("/static/users/vika91.png"));
        assertThat(dropdownMenu.getImageShape(), is(ImageShape.circle));

        assertThat(dropdownMenu.getSubItems().size(), is(5));
        assertThat(dropdownMenu.getSubItems().get(1).getType(), is("divider"));
        assertThat(dropdownMenu.getSubItems().get(3).getType(), is("divider"));
    }

    private void checkAction(Action action) {

    }
}
