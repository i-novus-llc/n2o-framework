package net.n2oapp.framework.config.metadata.application.menu;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.header.SimpleMenu;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationCompiler;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleMenuCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
        builder.ios(
                new NavMenuIOv3(),
                new ApplicationIOv3()
        );
        builder.compilers(
                new SimpleMenuCompiler(),
                new ApplicationCompiler()
        );
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/menu/testApplication.application.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/menu/testMenu.page.xml")
        );
    }

    @Test
    void testMenuItem() {
        Application application = read().compile().get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getMenu();
        MenuItem menuItem = menu.getItems().get(0);

        assertThat(menuItem.getId(), is("notif"));
        assertThat(menuItem.getSrc(), is("LinkMenuItem"));
        assertThat(menuItem.getTitle(), is("Уведомления"));
        assertThat(menuItem.getIcon(), is("fa fa-bell"));
        assertThat(menuItem.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(menuItem.getBadge().getText(), is("2"));
        assertThat(menuItem.getBadge().getColor(), is("warning"));
        assertThat(menuItem.getDatasource(), is("ds1"));
        assertThat(menuItem.getHref(), is("/login"));
        Page page = routeAndGet("/login", Page.class);
        assertThat(page, notNullValue());

        menuItem = menu.getItems().get(1);
        assertThat(menuItem.getTitle(), nullValue());
        assertThat(menuItem.getImageSrc(), is("/static/users/ivan90.png"));
        assertThat(menuItem.getImageShape(), is(ShapeTypeEnum.SQUARE));
        assertThat(menuItem.getHref(), is("/logout"));
    }

    @Test
    void testDropdownMenu() {
        Application application = read().compile().get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getMenu();
        MenuItem dropdownMenu = menu.getItems().get(3);

        // dropdown 1
        assertThat(dropdownMenu.getId(), is("user"));
        assertThat(dropdownMenu.getSrc(), is("DropdownMenuItem"));
        assertThat(dropdownMenu.getTitle(), is("Виктория"));
        assertThat(dropdownMenu.getImageSrc(), is("/static/users/vika91.png"));
        assertThat(dropdownMenu.getImageShape(), is(ShapeTypeEnum.CIRCLE));
        assertThat(dropdownMenu.getSubItems().size(), is(2));

        // dropdown 1 -> dropdown
        MenuItem subDropdownItem = dropdownMenu.getSubItems().get(0);
        assertThat(subDropdownItem.getId(), is("mi5"));
        assertThat(subDropdownItem.getSrc(), is("AnySrc"));
        assertThat(subDropdownItem.getTitle(), is("Отделы"));
        assertThat(subDropdownItem.getSubItems().size(), is(2));

        // dropdown 1 -> dropdown -> item
        MenuItem subMenuItem = subDropdownItem.getSubItems().get(0);
        assertThat(subMenuItem.getId(), is("mi6"));
        assertThat(subMenuItem.getSrc(), is("LinkMenuItem"));
        assertThat(subMenuItem.getHref(), is("/developers"));

        // dropdown 1 -> item
        subMenuItem = dropdownMenu.getSubItems().get(1);
        assertThat(subMenuItem.getId(), is("mi8"));
        assertThat(subMenuItem.getSrc(), is("LinkMenuItem"));
        assertThat(subMenuItem.getTitle(), is("Профиль"));
        assertThat(subMenuItem.getIcon(), is("fa fa-user"));
        assertThat(subMenuItem.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(subMenuItem.getHref(), is("/profile"));


        // dropdown 2
        dropdownMenu = menu.getItems().get(4);
        assertThat(dropdownMenu.getTitle(), is("Сообщения"));
        assertThat(dropdownMenu.getIcon(), is("fa fa-bell"));
        assertThat(dropdownMenu.getIconPosition(), is(PositionEnum.RIGHT));

        // dropdown 3 icon-position default value
        dropdownMenu = menu.getItems().get(5);
        assertThat(dropdownMenu.getTitle(), is("Добавить"));
        assertThat(dropdownMenu.getIcon(), is("fa fa-plus"));
        assertThat(dropdownMenu.getIconPosition(), is(PositionEnum.LEFT));
    }

    @Test
    void testMenuItemWithAction() {
        Application application = read().compile().get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getMenu();
        MenuItem menuItem = menu.getItems().get(2);

        assertThat(menuItem.getId(), is("alert"));
        assertThat(menuItem.getSrc(), is("ActionMenuItem"));
        assertThat(menuItem.getTitle(), is("Menu-item с алертом"));
        assertThat(menuItem.getAction(), instanceOf(AlertAction.class));
        ResponseMessage message = ((AlertActionPayload) ((AlertAction) menuItem.getAction()).getPayload()).getAlerts().get(0);
        assertThat(message.getSeverity(), is("success"));
        assertThat(message.getText(), is("Алерт"));
    }

    @Test
    void testExtraMenu() {
        Application application = read().compile().get(new ApplicationContext("testApplication"));
        SimpleMenu menu = application.getHeader().getExtraMenu();

        MenuItem menuItem = menu.getItems().get(0);
        assertThat(menuItem.getSrc(), is("Test"));
        assertThat(menuItem.getClassName(), is("class"));
        assertThat(menuItem.getStyle().get("background"), is("blue"));

        menuItem = menu.getItems().get(1);
        assertThat(menuItem.getSrc(), is("DropdownMenuItem"));

        menuItem = menu.getItems().get(1).getSubItems().get(0);
        assertThat(menuItem.getSrc(), is("LinkMenuItem"));

        menuItem = menu.getItems().get(1).getSubItems().get(1);
        assertThat(menuItem.getSrc(), is("ActionMenuItem"));
        assertThat(menuItem.getClassName(), is("text-center"));
        assertThat(menuItem.getStyle().get("background"), is("red"));

        menuItem = menu.getItems().get(1).getSubItems().get(2);
        assertThat(menuItem.getSrc(), is("StaticMenuItem"));
    }

    @Test
    void testValidateResolveNameNonDS() {
        N2oException exception = assertThrows(N2oException.class,
                () -> compile("net/n2oapp/framework/config/metadata/menu/testValidateResolveNameNonDS.application.xml")
                        .get(new ApplicationContext("testValidateResolveNameNonDS")));
        assertEquals("Меню имеет плейсхолдер 'name='{test}'', но при этом не указан источник данных", exception.getMessage());
    }

    @Test
    void testValidateResolveNameDropdownNonDS() {
        N2oException exception = assertThrows(N2oException.class,
                ()-> compile("net/n2oapp/framework/config/metadata/menu/testValidateResolveNameDropdownNonDS.application.xml")
                        .get(new ApplicationContext("testValidateResolveNameDropdownNonDS")));
        assertEquals("Меню имеет плейсхолдер 'name='{test}'', но при этом не указан источник данных", exception.getMessage());
    }

    @Test
    void testValidateResolveNameDS() {
        compile("net/n2oapp/framework/config/metadata/menu/testValidateResolveNameDS.application.xml")
                .get(new ApplicationContext("testValidateResolveNameDS"));
    }
}