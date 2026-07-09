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
import static org.hamcrest.Matchers.*;

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

        assertThat(link, allOf(
                hasProperty("src", is("Navigation/Link")),
                hasProperty("label", is("test label")),
                hasProperty("url", is("http://test.com")),
                hasProperty("icon", is("fa fa-bell")),
                hasProperty("iconPosition", is(PositionEnum.RIGHT)),
                hasProperty("target", is(TargetEnum.SELF)),
                hasProperty("visible", is("`1==2`")),
                hasProperty("enabled", is("`a==b`")),
                hasProperty("model", is(ReduxModelEnum.FILTER)),
                hasProperty("datasource", is("testNavRegionLinkMenuItem_ds")),
                hasProperty("className", is("class")),
                hasProperty("style", is(Map.of("backgroundColor", "blue")))
        ));

        Map<String, ModelLink> pathParam = link.getPathMapping();
        assertThat(pathParam.size(), is(1));
        assertThat(pathParam.get("id1").getValue(), is("`id1`"));
        assertThat(pathParam.get("id1").getDatasource(), is("testNavRegionLinkMenuItem_ds1"));
        assertThat(pathParam.get("id1").getModel(), is(ReduxModelEnum.EDIT));

        Map<String, ModelLink> queryParam = link.getQueryMapping();
        assertThat(queryParam.size(), is(1));
        assertThat(queryParam.get("id2"), allOf(
                hasProperty("value", is("`id2`")),
                hasProperty("datasource", is("testNavRegionLinkMenuItem_ds")),
                hasProperty("model", is(ReduxModelEnum.FILTER))
        ));

        GroupMenuItem group = (GroupMenuItem) region.getContent().get(1);

        BaseMenuItem subItem = group.getContent().getFirst();
        assertThat(subItem, instanceOf(LinkMenuItem.class));
        link = (LinkMenuItem) subItem;
        assertThat(link, allOf(
                hasProperty("src", is("Navigation/Link")),
                hasProperty("label", is("sublabel")),
                hasProperty("url", is("/person/:person_id/docs")),
                hasProperty("icon", is("fa fa-home")),
                hasProperty("iconPosition", is(PositionEnum.LEFT)),
                hasProperty("target", is(TargetEnum.SELF)),
                hasProperty("newWindow", is(true)),
                hasProperty("visible", is(false)),
                hasProperty("enabled", nullValue()),
                hasProperty("model", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("datasource", is("testNavRegionLinkMenuItem_ds")),
                hasProperty("className", is("class1")),
                hasProperty("style", is(Map.of("color", "red")))
        ));

        pathParam = link.getPathMapping();
        assertThat(pathParam.size(), is(0));

        queryParam = link.getQueryMapping();
        assertThat(queryParam.size(), is(1));
        assertThat(queryParam.get("id"), allOf(
                hasProperty("value", is("`id`")),
                hasProperty("datasource", is("testNavRegionLinkMenuItem_ds")),
                hasProperty("model", is(ReduxModelEnum.DATASOURCE))
        ));
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
        assertThat(group, allOf(
                hasProperty("src", is("Navigation/Group")),
                hasProperty("label", is("test label")),
                hasProperty("collapsible", is(false)),
                hasProperty("defaultState", is(N2oGroupMenuItem.GroupStateTypeEnum.COLLAPSED)),
                hasProperty("icon", is("fa fa-bell")),
                hasProperty("iconPosition", is(PositionEnum.RIGHT)),
                hasProperty("visible", is("`1==2`")),
                hasProperty("enabled", is("`a==b`")),
                hasProperty("datasource", is("testNavRegionGroupMenuItem_ds")),
                hasProperty("model", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("className", is("class")),
                hasProperty("style", is(Map.of("backgroundColor", "blue")))
        ));

        assertThat(group.getContent().size(), is(2));

        menuItem = group.getContent().getFirst();
        assertThat(menuItem, allOf(
                hasProperty("src", is("Navigation/Link")),
                hasProperty("model", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("datasource", is("testNavRegionGroupMenuItem_ds"))
        ));

        menuItem = group.getContent().get(1);
        assertThat(menuItem.getSrc(), is("Navigation/Group"));

        group = (GroupMenuItem) group.getContent().get(1);
        assertThat(group, allOf(
                hasProperty("label", is("sublabel")),
                hasProperty("collapsible", is(true)),
                hasProperty("defaultState", is(N2oGroupMenuItem.GroupStateTypeEnum.EXPANDED)),
                hasProperty("icon", is("fa fa-home")),
                hasProperty("iconPosition", is(PositionEnum.LEFT)),
                hasProperty("visible", is(false)),
                hasProperty("enabled", nullValue()),
                hasProperty("model", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("datasource", is("testNavRegionGroupMenuItem_ds")),
                hasProperty("className", is("class1")),
                hasProperty("style", is(Map.of("color", "red")))
        ));

        DropdownMenuItem dropdown = (DropdownMenuItem) group.getContent().get(2);
        group = (GroupMenuItem) dropdown.getContent().getFirst();
        assertThat(group, allOf(
                hasProperty("label", nullValue()),
                hasProperty("collapsible", is(true)),
                hasProperty("defaultState", is(N2oGroupMenuItem.GroupStateTypeEnum.EXPANDED)),
                hasProperty("icon", nullValue()),
                hasProperty("iconPosition", is(PositionEnum.LEFT)),
                hasProperty("visible", nullValue()),
                hasProperty("enabled", nullValue()),
                hasProperty("model", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("datasource", is("testNavRegionGroupMenuItem_ds")),
                hasProperty("className", nullValue()),
                hasProperty("style", nullValue())
        ));

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

        assertThat(item, allOf(
                hasProperty("id", is("notif")),
                hasProperty("src", is("Buttons/MenuItem")),
                hasProperty("label", is("Уведомления")),
                hasProperty("icon", is("fa fa-bell")),
                hasProperty("iconPosition", is(PositionEnum.RIGHT)),
                hasProperty("datasource", is("testNavRegionMenuItem_ds1")),
                hasProperty("className", is("class")),
                hasProperty("style", is(Map.of("backgroundColor", "blue")))
        ));

        assertThat(item.getBadge(), allOf(
                hasProperty("text", is("2")),
                hasProperty("color", is("warning"))
        ));
        assertThat(((LinkAction) item.getAction()).getUrl(), is("/testNavRegionMenuItem/login/"));

        menuItem = (BaseMenuItem) nav.getContent().get(1);
        assertThat(menuItem, instanceOf(MenuItem.class));
        item = (MenuItem) menuItem;
        assertThat(item, allOf(
                hasProperty("label", nullValue()),
                hasProperty("imageSrc", is("/static/users/ivan90.png")),
                hasProperty("imageShape", is(ShapeTypeEnum.SQUARE))
        ));
        assertThat(((LinkAction) item.getAction()).getUrl(), is("/testNavRegionMenuItem/logout/"));

        menuItem = (BaseMenuItem) nav.getContent().get(2);
        assertThat(menuItem, instanceOf(MenuItem.class));
        item = (MenuItem) menuItem;
        assertThat(item, allOf(
                hasProperty("id", is("alert")),
                hasProperty("label", is("Menu-item с алертом")),
                hasProperty("action", instanceOf(MultiAction.class))
        ));
        MultiAction multiAction = (MultiAction) item.getAction();
        assertThat(multiAction.getType(), is("n2o/api/action/sequence"));
        List<Action> actions = multiAction.getPayload().getActions();
        assertThat(actions.size(), is(3));
        assertThat(actions.getFirst(), instanceOf(AlertAction.class));
        ResponseMessage message = ((AlertActionPayload) ((AlertAction) actions.getFirst()).getPayload()).getAlerts().getFirst();
        assertThat(message, allOf(
                hasProperty("severity", is("success")),
                hasProperty("text", is("Алерт"))
        ));
        assertThat(actions.get(1), instanceOf(LinkActionImpl.class));
        assertThat((LinkActionImpl) actions.get(1), allOf(
                hasProperty("url", is("/testNavRegionMenuItem/logout/")),
                hasProperty("target", is(TargetEnum.APPLICATION)),
                hasProperty("pageId", is("testMenu"))
        ));
        assertThat(actions.get(2), instanceOf(LinkAction.class));
        assertThat((LinkActionImpl) actions.get(2), allOf(
                hasProperty("url", is("/test2")),
                hasProperty("target", is(TargetEnum.SELF))
        ));
        item = (MenuItem) nav.getContent().get(3);
        assertThat(item, allOf(
                hasProperty("id", is("mi3")),
                hasProperty("label", is("Пункт меню")),
                hasProperty("action", instanceOf(AlertAction.class))
        ));
        message = ((AlertActionPayload) ((AlertAction) item.getAction()).getPayload()).getAlerts().getFirst();
        assertThat(message.getText(), is("alert1"));

        item = (MenuItem) nav.getContent().get(4);
        assertThat(item, allOf(
                hasProperty("id", is("mi4")),
                hasProperty("label", is("Пункт меню c MultiAction"))
        ));
        multiAction = (MultiAction) item.getAction();
        assertThat(multiAction.getType(), Matchers.is("n2o/api/action/sequence"));
        List<Action> actionList = multiAction.getPayload().getActions();
        assertThat(actionList, contains(
                instanceOf(AlertAction.class),
                instanceOf(SetValueAction.class),
                instanceOf(LinkAction.class)
        ));
        assertThat((LinkAction) actionList.get(2), allOf(
                hasProperty("url", is("/test1")),
                hasProperty("target", is(TargetEnum.APPLICATION))
        ));
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
        assertThat(dropdownMenu, allOf(
                hasProperty("id", is("user")),
                hasProperty("src", is("Navigation/Dropdown")),
                hasProperty("label", is("Виктория")),
                hasProperty("trigger", is(TriggerEnum.HOVER)),
                hasProperty("position", is(N2oDropdownMenuItem.PositionTypeEnum.RIGHT)),
                hasProperty("imageSrc", is("/static/users/vika91.png")),
                hasProperty("imageShape", is(ShapeTypeEnum.CIRCLE)),
                hasProperty("content", hasSize(2)),
                hasProperty("className", is("class")),
                hasProperty("style", is(Map.of("backgroundColor", "blue")))
        ));

        // dropdown 1 -> dropdown
        DropdownMenuItem subDropdownItem = (DropdownMenuItem) dropdownMenu.getContent().getFirst();
        assertThat(subDropdownItem, allOf(
                hasProperty("id", is("mi1")),
                hasProperty("src", is("AnySrc")),
                hasProperty("label", is("Отделы")),
                hasProperty("trigger", is(TriggerEnum.HOVER)),
                hasProperty("position", is(N2oDropdownMenuItem.PositionTypeEnum.LEFT))
        ));
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

        assertThat(button, allOf(
                hasProperty("src", is("Buttons/Button")),
                hasProperty("label", is("test label")),
                hasProperty("icon", is("fa fa-bell")),
                hasProperty("iconPosition", is(PositionEnum.RIGHT)),
                hasProperty("visible", is("`1==2`")),
                hasProperty("enabled", is("`a==b`")),
                hasProperty("model", is(ReduxModelEnum.EDIT)),
                hasProperty("datasource", is("testNavRegionButtonMenuItem_ds1")),
                hasProperty("className", is("class")),
                hasProperty("style", is(Map.of("backgroundColor", "blue")))
        ));

        Action action = button.getAction();
        assertThat(action, instanceOf(MultiAction.class));
        MultiAction multiAction = (MultiAction) action;
        assertThat(multiAction.getType(), is("n2o/api/action/sequence"));
        List<Action> actionList = multiAction.getPayload().getActions();
        assertThat(actionList, contains(
                instanceOf(CustomAction.class),
                instanceOf(LinkAction.class)
        ));
        assertThat(((LinkAction) actionList.get(1)).getUrl(), is("/test2"));
        assertThat(((LinkAction) actionList.get(1)).getTarget(), is(TargetEnum.APPLICATION));

        GroupMenuItem group = (GroupMenuItem) nav.getContent().get(1);

        BaseMenuItem subItem = group.getContent().getFirst();
        assertThat(subItem, instanceOf(ButtonMenuItem.class));
        button = (ButtonMenuItem) subItem;
        assertThat(button, allOf(
                hasProperty("src", is("Buttons/Button")),
                hasProperty("label", nullValue()),
                hasProperty("icon", nullValue()),
                hasProperty("iconPosition", is(PositionEnum.LEFT)),
                hasProperty("visible", nullValue()),
                hasProperty("enabled", nullValue()),
                hasProperty("model", is(ReduxModelEnum.DATASOURCE)),
                hasProperty("datasource", is("testNavRegionButtonMenuItem_ds")),
                hasProperty("className", nullValue()),
                hasProperty("style", nullValue())
        ));

        action = button.getAction();
        assertThat(action, instanceOf(AlertAction.class));
        AlertAction alertAction = (AlertAction) action;
        assertThat(((AlertActionPayload) alertAction.getPayload()).getAlerts().getFirst().getText(), is("message"));

        button = (ButtonMenuItem) nav.getContent().get(2);
        assertThat(button, allOf(
                hasProperty("id", is("mi3")),
                hasProperty("label", is("Пункт меню")),
                hasProperty("action", instanceOf(AlertAction.class))
        ));
        ResponseMessage message = ((AlertActionPayload) ((AlertAction) button.getAction()).getPayload()).getAlerts().getFirst();
        assertThat(message.getText(), is("alert1"));

        button = (ButtonMenuItem) nav.getContent().get(3);
        assertThat(button.getId(), is("mi4"));
        assertThat(button.getLabel(), is("Пункт меню c MultiAction"));
        multiAction = (MultiAction) button.getAction();
        assertThat(multiAction.getType(), Matchers.is("n2o/api/action/sequence"));
        actionList = multiAction.getPayload().getActions();
        assertThat(actionList, contains(
                instanceOf(AlertAction.class),
                instanceOf(SetValueAction.class),
                instanceOf(LinkAction.class)
        ));
        assertThat(((LinkAction) actionList.get(2)).getUrl(), Matchers.is("/test1"));
        assertThat(((LinkAction) actionList.get(2)).getTarget(), Matchers.is(TargetEnum.APPLICATION));
    }
}
