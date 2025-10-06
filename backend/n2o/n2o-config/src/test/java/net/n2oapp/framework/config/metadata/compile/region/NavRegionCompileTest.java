package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.menu.N2oDropdownMenuItem;
import net.n2oapp.framework.api.metadata.menu.N2oGroupMenuItem;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomAction;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.menu.*;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.NavRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование компиляции региона {@code <nav>}
 */
class NavRegionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    void testDividerMenuItem() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testNavRegionDividerMenuItem.page.xml")
                .get(new PageContext("testNavRegionDividerMenuItem"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));

        NavRegion region = (NavRegion) regions.getFirst();
        assertThat(region.getContent().getFirst(), instanceOf(LinkMenuItem.class));
        assertThat(region.getContent().get(1), instanceOf(DividerMenuItem.class));
        assertThat(((BaseMenuItem) region.getContent().get(1)).getClassName(), is("class1"));
        assertThat(((BaseMenuItem) region.getContent().get(1)).getStyle(), is(Map.of("color", "red")));

        assertThat(region.getContent().get(2), instanceOf(GroupMenuItem.class));
        GroupMenuItem group = (GroupMenuItem) region.getContent().get(2);
        assertThat(group.getContent().getFirst(), instanceOf(DividerMenuItem.class));
        assertThat(group.getContent().getFirst().getClassName(), is("class2"));
        assertThat(group.getContent().getFirst().getStyle(), is(Map.of("backgroundColor", "blue")));
    }

    @Test
    void testLinkMenuItem() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testNavRegionLinkMenuItem.page.xml")
                .get(new PageContext("testNavRegionLinkMenuItem"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));
        NavRegion region = (NavRegion) regions.getFirst();

        BaseMenuItem item = (BaseMenuItem) region.getContent().getFirst();
        assertThat(item, instanceOf(LinkMenuItem.class));
        LinkMenuItem link = (LinkMenuItem) region.getContent().getFirst();

        assertThat(link.getSrc(), is("Navigation/Link"));
        assertThat(link.getLabel(), is("test label"));
        assertThat(link.getUrl(), is("http://test.com"));
        assertThat(link.getIcon(), is("fa fa-bell"));
        assertThat(link.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(link.getTarget(), is(TargetEnum.SELF));
        assertThat(link.getVisible(), is("`1==2`"));
        assertThat(link.getEnabled(), is("`a==b`"));
        assertThat(link.getModel(), is(ReduxModelEnum.FILTER));
        assertThat(link.getDatasource(), is("testNavRegionLinkMenuItem_ds"));
        assertThat(link.getClassName(), is("class"));
        assertThat(link.getStyle(), is(Map.of("backgroundColor", "blue")));

        Map<String, ModelLink> pathParam = link.getPathMapping();
        assertThat(pathParam.size(), is(1));
        assertThat(pathParam.get("id1").getValue(), is("`id1`"));
        assertThat(pathParam.get("id1").getDatasource(), is("testNavRegionLinkMenuItem_ds1"));
        assertThat(pathParam.get("id1").getModel(), is(ReduxModelEnum.EDIT));

        Map<String, ModelLink> queryParam = link.getQueryMapping();
        assertThat(queryParam.size(), is(1));
        assertThat(queryParam.get("id2").getValue(), is("`id2`"));
        assertThat(queryParam.get("id2").getDatasource(), is("testNavRegionLinkMenuItem_ds"));
        assertThat(queryParam.get("id2").getModel(), is(ReduxModelEnum.FILTER));

        GroupMenuItem group = (GroupMenuItem) region.getContent().get(1);

        BaseMenuItem subItem = group.getContent().getFirst();
        assertThat(subItem, instanceOf(LinkMenuItem.class));
        link = (LinkMenuItem) subItem;
        assertThat(link.getSrc(), is("Navigation/Link"));
        assertThat(link.getLabel(), is("sublabel"));
        assertThat(link.getUrl(), is("/person/:person_id/docs"));
        assertThat(link.getIcon(), is("fa fa-home"));
        assertThat(link.getIconPosition(), is(PositionEnum.LEFT));
        assertThat(link.getTarget(), is(TargetEnum.NEW_WINDOW));
        assertThat(link.getVisible(), is(false));
        assertThat(link.getEnabled(), nullValue());
        assertThat(link.getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(link.getDatasource(), is("testNavRegionLinkMenuItem_ds"));
        assertThat(link.getClassName(), is("class1"));
        assertThat(link.getStyle(), is(Map.of("color", "red")));

        pathParam = link.getPathMapping();
        assertThat(pathParam.size(), is(0));

        queryParam = link.getQueryMapping();
        assertThat(queryParam.size(), is(1));
        assertThat(queryParam.get("id").getValue(), is("`id`"));
        assertThat(queryParam.get("id").getDatasource(), is("testNavRegionLinkMenuItem_ds"));
        assertThat(queryParam.get("id").getModel(), is(ReduxModelEnum.DATASOURCE));
    }

    @Test
    void testGroupMenuItem() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testNavRegionGroupMenuItem.page.xml")
                .get(new PageContext("testNavRegionGroupMenuItem"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));
        NavRegion nav = (NavRegion) regions.getFirst();

        assertThat(nav.getDatasource(), is("testNavRegionGroupMenuItem_ds"));
        assertThat(nav.getModel(), is(ReduxModelEnum.DATASOURCE));

        BaseMenuItem menuItem = (BaseMenuItem) nav.getContent().getFirst();
        assertThat(menuItem, instanceOf(GroupMenuItem.class));

        GroupMenuItem group = (GroupMenuItem) menuItem;
        assertThat(group.getSrc(), is("Navigation/Group"));
        assertThat(group.getLabel(), is("test label"));
        assertThat(group.getCollapsible(), is(false));
        assertThat(group.getDefaultState(), is(N2oGroupMenuItem.GroupStateTypeEnum.COLLAPSED));
        assertThat(group.getIcon(), is("fa fa-bell"));
        assertThat(group.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(group.getVisible(), is("`1==2`"));
        assertThat(group.getEnabled(), is("`a==b`"));
        assertThat(group.getDatasource(), is("testNavRegionGroupMenuItem_ds"));
        assertThat(group.getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(group.getClassName(), is("class"));
        assertThat(group.getStyle(), is(Map.of("backgroundColor", "blue")));

        assertThat(group.getContent().size(), is(2));

        menuItem = group.getContent().getFirst();
        assertThat(menuItem.getSrc(), is("Navigation/Link"));
        assertThat(menuItem.getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(menuItem.getDatasource(), is("testNavRegionGroupMenuItem_ds"));

        menuItem = group.getContent().get(1);
        assertThat(menuItem.getSrc(), is("Navigation/Group"));

        group = (GroupMenuItem) group.getContent().get(1);
        assertThat(group.getLabel(), is("sublabel"));
        assertThat(group.getCollapsible(), is(true));
        assertThat(group.getDefaultState(), is(N2oGroupMenuItem.GroupStateTypeEnum.EXPANDED));
        assertThat(group.getIcon(), is("fa fa-home"));
        assertThat(group.getIconPosition(), is(PositionEnum.LEFT));
        assertThat(group.getVisible(), is(false));
        assertThat(group.getEnabled(), nullValue());
        assertThat(group.getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(group.getDatasource(), is("testNavRegionGroupMenuItem_ds"));
        assertThat(group.getClassName(), is("class1"));
        assertThat(group.getStyle(), is(Map.of("color", "red")));

        DropdownMenuItem dropdown = (DropdownMenuItem) group.getContent().get(2);
        group = (GroupMenuItem) dropdown.getContent().getFirst();
        assertThat(group.getLabel(), nullValue());
        assertThat(group.getCollapsible(), is(true));
        assertThat(group.getDefaultState(), is(N2oGroupMenuItem.GroupStateTypeEnum.EXPANDED));
        assertThat(group.getIcon(), nullValue());
        assertThat(group.getIconPosition(), is(PositionEnum.LEFT));
        assertThat(group.getVisible(), nullValue());
        assertThat(group.getEnabled(), nullValue());
        assertThat(group.getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(group.getDatasource(), is("testNavRegionGroupMenuItem_ds"));
        assertThat(group.getClassName(), nullValue());
        assertThat(group.getStyle(), nullValue());

        menuItem = group.getContent().getFirst();
        assertThat(menuItem, instanceOf(MenuItem.class));
    }

    @Test
    void testMenuItem() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testNavRegionMenuItem.page.xml",
                "net/n2oapp/framework/config/metadata/menu/testMenu.page.xml")
                .get(new PageContext("testNavRegionMenuItem"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));
        NavRegion nav = (NavRegion) regions.getFirst();

        BaseMenuItem menuItem = (BaseMenuItem) nav.getContent().getFirst();
        assertThat(menuItem, instanceOf(MenuItem.class));
        MenuItem item = (MenuItem) menuItem;
        assertThat(item.getId(), is("notif"));
        assertThat(item.getSrc(), is("Buttons/MenuItem"));
        assertThat(item.getLabel(), is("Уведомления"));
        assertThat(item.getIcon(), is("fa fa-bell"));
        assertThat(item.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(item.getBadge().getText(), is("2"));
        assertThat(item.getBadge().getColor(), is("warning"));
        assertThat(item.getDatasource(), is("testNavRegionMenuItem_ds1"));
        assertThat(((LinkAction) item.getAction()).getUrl(), is("/testNavRegionMenuItem/login"));

        menuItem = (BaseMenuItem) nav.getContent().get(1);
        assertThat(menuItem, instanceOf(MenuItem.class));
        item = (MenuItem) menuItem;
        assertThat(item.getLabel(), nullValue());
        assertThat(item.getImageSrc(), is("/static/users/ivan90.png"));
        assertThat(item.getImageShape(), is(ShapeTypeEnum.SQUARE));
        assertThat(((LinkAction) item.getAction()).getUrl(), is("/testNavRegionMenuItem/logout"));

        menuItem = (BaseMenuItem) nav.getContent().get(2);
        assertThat(menuItem, instanceOf(MenuItem.class));
        item = (MenuItem) menuItem;
        assertThat(item.getId(), is("alert"));
        assertThat(item.getLabel(), is("Menu-item с алертом"));
        assertThat(item.getAction(), instanceOf(MultiAction.class));
        MultiAction multiAction = (MultiAction) item.getAction();
        assertThat(multiAction.getType(), is("n2o/api/action/sequence"));
        List<Action> actions = multiAction.getPayload().getActions();
        assertThat(actions.size(), is(3));
        assertThat(actions.getFirst(), instanceOf(AlertAction.class));
        ResponseMessage message = ((AlertActionPayload) ((AlertAction) actions.getFirst()).getPayload()).getAlerts().getFirst();
        assertThat(message.getSeverity(), is("success"));
        assertThat(message.getText(), is("Алерт"));
        assertThat(actions.get(1), instanceOf(LinkActionImpl.class));
        assertThat(((LinkActionImpl) actions.get(1)).getUrl(), is("/testNavRegionMenuItem/logout"));
        assertThat(((LinkActionImpl) actions.get(1)).getTarget(), is(TargetEnum.APPLICATION));
        assertThat(((LinkActionImpl) actions.get(1)).getPageId(), is("testMenu"));
        assertThat(actions.get(2), instanceOf(LinkAction.class));
        assertThat(((LinkAction) actions.get(2)).getUrl(), is("/test2"));
        assertThat(((LinkAction) actions.get(2)).getTarget(), is(TargetEnum.NEW_WINDOW));

        item = (MenuItem) nav.getContent().get(3);
        assertThat(item.getId(), is("mi3"));
        assertThat(item.getLabel(), is("Пункт меню"));
        assertThat(item.getAction(), instanceOf(AlertAction.class));
        message = ((AlertActionPayload) ((AlertAction) item.getAction()).getPayload()).getAlerts().getFirst();
        assertThat(message.getText(), is("alert1"));

        item = (MenuItem) nav.getContent().get(4);
        assertThat(item.getId(), is("mi4"));
        assertThat(item.getLabel(), is("Пункт меню c MultiAction"));
        multiAction = (MultiAction) item.getAction();
        assertThat(multiAction.getType(), Matchers.is("n2o/api/action/sequence"));
        assertThat(multiAction.getPayload().getActions().size(), Matchers.is(3));
        assertThat(multiAction.getPayload().getActions().getFirst(), instanceOf(AlertAction.class));
        assertThat(multiAction.getPayload().getActions().get(1), instanceOf(SetValueAction.class));
        assertThat(multiAction.getPayload().getActions().get(2), instanceOf(LinkAction.class));
        assertThat(((LinkAction) multiAction.getPayload().getActions().get(2)).getUrl(), Matchers.is("/test1"));
        assertThat(((LinkAction) multiAction.getPayload().getActions().get(2)).getTarget(), Matchers.is(TargetEnum.APPLICATION));
    }

    @Test
    void testDropdownMenuItem() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testNavRegionDropdownMenuItem.page.xml")
                .get(new PageContext("testNavRegionDropdownMenuItem"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));
        NavRegion nav = (NavRegion) regions.getFirst();

        DropdownMenuItem dropdownMenu = (DropdownMenuItem) nav.getContent().getFirst();

        // dropdown 1
        assertThat(dropdownMenu.getId(), is("user"));
        assertThat(dropdownMenu.getSrc(), is("Navigation/Dropdown"));
        assertThat(dropdownMenu.getLabel(), is("Виктория"));
        assertThat(dropdownMenu.getTrigger(), is(TriggerEnum.HOVER));
        assertThat(dropdownMenu.getPosition(), is(N2oDropdownMenuItem.PositionTypeEnum.RIGHT));
        assertThat(dropdownMenu.getImageSrc(), is("/static/users/vika91.png"));
        assertThat(dropdownMenu.getImageShape(), is(ShapeTypeEnum.CIRCLE));
        assertThat(dropdownMenu.getContent().size(), is(2));

        // dropdown 1 -> dropdown
        DropdownMenuItem subDropdownItem = (DropdownMenuItem) dropdownMenu.getContent().getFirst();
        assertThat(subDropdownItem.getId(), is("mi1"));
        assertThat(subDropdownItem.getSrc(), is("AnySrc"));
        assertThat(subDropdownItem.getLabel(), is("Отделы"));
        assertThat(subDropdownItem.getTrigger(), is(TriggerEnum.HOVER));
        assertThat(subDropdownItem.getPosition(), is(N2oDropdownMenuItem.PositionTypeEnum.LEFT));
        assertThat(subDropdownItem.getContent().size(), is(2));

        // dropdown 1 -> dropdown -> item
        MenuItem subMenuItem = (MenuItem) subDropdownItem.getContent().getFirst();
        assertThat(subMenuItem.getId(), is("mi2"));
        assertThat(subMenuItem.getSrc(), is("Buttons/MenuItem"));
        assertThat(((LinkAction) subMenuItem.getAction()).getUrl(), is("/developers"));

        // dropdown 1 -> item
        subMenuItem = (MenuItem) dropdownMenu.getContent().get(1);
        assertThat(subMenuItem.getId(), is("mi4"));
        assertThat(subMenuItem.getSrc(), is("Buttons/MenuItem"));
        assertThat(subMenuItem.getLabel(), is("Профиль"));
        assertThat(subMenuItem.getIcon(), is("fa fa-user"));
        assertThat(subMenuItem.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(((LinkAction) subMenuItem.getAction()).getUrl(), is("/profile"));

        // dropdown 2
        dropdownMenu = (DropdownMenuItem) nav.getContent().get(1);
        assertThat(dropdownMenu.getLabel(), is("Сообщения"));
        assertThat(dropdownMenu.getIcon(), is("fa fa-bell"));
        assertThat(dropdownMenu.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(dropdownMenu.getTrigger(), is(TriggerEnum.CLICK));
        assertThat(dropdownMenu.getPosition(), is(N2oDropdownMenuItem.PositionTypeEnum.AUTO));

        // dropdown 3 icon-position default value
        dropdownMenu = (DropdownMenuItem) nav.getContent().get(2);
        assertThat(dropdownMenu.getLabel(), is("Добавить"));
        assertThat(dropdownMenu.getIcon(), is("fa fa-plus"));
        assertThat(dropdownMenu.getIconPosition(), is(PositionEnum.LEFT));
    }

    @Test
    void testButtonMenuItem() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testNavRegionButtonMenuItem.page.xml")
                .get(new PageContext("testNavRegionButtonMenuItem"));
        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));
        NavRegion nav = (NavRegion) regions.getFirst();

        ButtonMenuItem button = (ButtonMenuItem) nav.getContent().getFirst();

        assertThat(button.getSrc(), is("Buttons/Button"));
        assertThat(button.getLabel(), is("test label"));
        assertThat(button.getIcon(), is("fa fa-bell"));
        assertThat(button.getIconPosition(), is(PositionEnum.RIGHT));
        assertThat(button.getVisible(), is("`1==2`"));
        assertThat(button.getEnabled(), is("`a==b`"));
        assertThat(button.getModel(), is(ReduxModelEnum.EDIT));
        assertThat(button.getDatasource(), is("testNavRegionButtonMenuItem_ds1"));
        assertThat(button.getClassName(), is("class"));
        assertThat(button.getStyle(), is(Map.of("backgroundColor", "blue")));

        Action action = button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        MultiAction multiAction = (MultiAction) action;
        assertThat(multiAction.getType(), is("n2o/api/action/sequence"));
        assertThat(multiAction.getPayload().getActions().size(), is(2));
        assertThat(multiAction.getPayload().getActions().getFirst(), instanceOf(CustomAction.class));
        assertThat(multiAction.getPayload().getActions().get(1), instanceOf(LinkAction.class));
        assertThat(((LinkAction) multiAction.getPayload().getActions().get(1)).getUrl(), is("/test2"));
        assertThat(((LinkAction) multiAction.getPayload().getActions().get(1)).getTarget(), is(TargetEnum.APPLICATION));

        GroupMenuItem group = (GroupMenuItem) nav.getContent().get(1);

        BaseMenuItem subItem = group.getContent().getFirst();
        assertThat(subItem, instanceOf(ButtonMenuItem.class));
        button = (ButtonMenuItem) subItem;
        assertThat(button.getSrc(), is("Buttons/Button"));
        assertThat(button.getLabel(), nullValue());
        assertThat(button.getIcon(), nullValue());
        assertThat(button.getIconPosition(), is(PositionEnum.LEFT));
        assertThat(button.getVisible(), nullValue());
        assertThat(button.getEnabled(), nullValue());
        assertThat(button.getModel(), is(ReduxModelEnum.DATASOURCE));
        assertThat(button.getDatasource(), is("testNavRegionButtonMenuItem_ds"));
        assertThat(button.getClassName(), nullValue());
        assertThat(button.getStyle(), nullValue());

        action = button.getAction();
        assertThat(action, instanceOf(AlertAction.class));
        AlertAction alertAction = (AlertAction) action;
        assertThat(((AlertActionPayload) alertAction.getPayload()).getAlerts().getFirst().getText(), is("message"));

        button = (ButtonMenuItem) nav.getContent().get(2);
        assertThat(button.getId(), is("mi3"));
        assertThat(button.getLabel(), is("Пункт меню"));
        assertThat(button.getAction(), instanceOf(AlertAction.class));
        ResponseMessage message = ((AlertActionPayload) ((AlertAction) button.getAction()).getPayload()).getAlerts().getFirst();
        assertThat(message.getText(), is("alert1"));

        button = (ButtonMenuItem) nav.getContent().get(3);
        assertThat(button.getId(), is("mi4"));
        assertThat(button.getLabel(), is("Пункт меню c MultiAction"));
        multiAction = (MultiAction) button.getAction();
        assertThat(multiAction.getType(), Matchers.is("n2o/api/action/sequence"));
        assertThat(multiAction.getPayload().getActions().size(), Matchers.is(3));
        assertThat(multiAction.getPayload().getActions().getFirst(), instanceOf(AlertAction.class));
        assertThat(multiAction.getPayload().getActions().get(1), instanceOf(SetValueAction.class));
        assertThat(multiAction.getPayload().getActions().get(2), instanceOf(LinkAction.class));
        assertThat(((LinkAction) multiAction.getPayload().getActions().get(2)).getUrl(), Matchers.is("/test1"));
        assertThat(((LinkAction) multiAction.getPayload().getActions().get(2)).getTarget(), Matchers.is(TargetEnum.APPLICATION));
    }
}