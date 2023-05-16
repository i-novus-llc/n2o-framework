package net.n2oapp.framework.config.metadata.application.menu;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv2;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationCompiler;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SimpleMenuCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
        builder.ios(new NavMenuIOv3(), new ApplicationIOv2());
        builder.compilers(new SimpleMenuCompiler(), new ApplicationCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/menu/testApplication.application.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/menu/testMenu.page.xml"));
    }

    @Test
    void testMenuItem() {
        Application application = read().compile().get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getMenu();
        MenuItem menuItem = menu.getItems().get(0);

        assertThat(menuItem.getId(), is("notif"));
        assertThat(menuItem.getType(), is("link"));
        assertThat(menuItem.getTitle(), is("Уведомления"));
        assertThat(menuItem.getIcon(), is("fa fa-bell"));
        assertThat(menuItem.getBadge().getText(), is("2"));
        assertThat(menuItem.getBadge().getColor(), is("warning"));
        assertThat(menuItem.getDatasource(), is("ds1"));
        assertThat(menuItem.getHref(), is("/login"));
        Page page = routeAndGet("/login", Page.class);
        assertThat(page, notNullValue());

        menuItem = menu.getItems().get(1);
        assertThat(menuItem.getTitle(), nullValue());
        assertThat(menuItem.getImageSrc(), is("/static/users/ivan90.png"));
        assertThat(menuItem.getImageShape(), is(ShapeType.SQUARE));
        assertThat(menuItem.getHref(), is("/logout"));
    }

    @Test
    void testDropdownMenu() {
        Application application = read().compile().get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getMenu();
        MenuItem dropdownMenu = menu.getItems().get(2);

        // dropdown 1
        assertThat(dropdownMenu.getId(), is("user"));
        assertThat(dropdownMenu.getType(), is("dropdown"));
        assertThat(dropdownMenu.getTitle(), is("Виктория"));
        assertThat(dropdownMenu.getImageSrc(), is("/static/users/vika91.png"));
        assertThat(dropdownMenu.getImageShape(), is(ShapeType.CIRCLE));
        assertThat(dropdownMenu.getSubItems().size(), is(2));

        // dropdown 1 -> dropdown
        MenuItem subDropdownItem = dropdownMenu.getSubItems().get(0);
        assertThat(subDropdownItem.getId(), is("mi4"));
        assertThat(subDropdownItem.getType(), is("dropdown"));
        assertThat(subDropdownItem.getTitle(), is("Отделы"));
        assertThat(subDropdownItem.getSubItems().size(), is(2));

        // dropdown 1 -> dropdown -> item
        MenuItem subMenuItem = subDropdownItem.getSubItems().get(0);
        assertThat(subMenuItem.getId(), is("mi5"));
        assertThat(subMenuItem.getType(), is("link"));
        assertThat(subMenuItem.getHref(), is("/developers"));

        // dropdown 1 -> item
        subMenuItem = dropdownMenu.getSubItems().get(1);
        assertThat(subMenuItem.getId(), is("mi7"));
        assertThat(subMenuItem.getType(), is("link"));
        assertThat(subMenuItem.getTitle(), is("Профиль"));
        assertThat(subMenuItem.getIcon(), is("fa fa-user"));
        assertThat(subMenuItem.getHref(), is("/profile"));


        // dropdown 2
        dropdownMenu = menu.getItems().get(3);
        assertThat(dropdownMenu.getTitle(), is("Сообщения"));
        assertThat(dropdownMenu.getIcon(), is("fa fa-bell"));
    }
}
