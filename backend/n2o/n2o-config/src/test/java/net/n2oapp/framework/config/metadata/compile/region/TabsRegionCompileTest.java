package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.meta.action.SetActiveRegionEntityPayload;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Тестирование компиляции региона в виде вкладок
 */
class TabsRegionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack()
        );
    }

    @Test
    void testTabsRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegion.page.xml")
                .get(new PageContext("testTabsRegion"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(4));

        // TABS1
        checkTabs1(regions.get(0));

        // TABS2
        checkTabs2(regions.get(1));

        // TABS3
        TabsRegion region = (TabsRegion) regions.get(2);
        assertThat(region.getId(), is("testTabsRegion_tabs6"));
        assertThat(region.getItems().size(), is(1));
        assertThat(region.getItems().get(0).getId(), is("testTabsRegion_tab7"));
        assertThat(region.getItems().get(0).getContent(), nullValue());

        // TABS4
        region = (TabsRegion) regions.get(3);
        assertThat(region.getRoutable(), is(false));
    }


    private static void checkTabs1(Region region) {
        assertThat(region, allOf(
                instanceOf(TabsRegion.class),
                hasProperty("id", is("testTabsRegion_tabs0")),
                hasProperty("src", is("TabsRegion")),
                hasProperty("alwaysRefresh", is(false)),
                hasProperty("lazy", is(true)),
                hasProperty("hideSingleTab", is(false)),
                hasProperty("maxHeight", nullValue()),
                hasProperty("scrollbar", is(false))
        ));
        List<TabsRegion.Tab> items = ((TabsRegion) region).getItems();
        assertThat(items.size(), is(1));
        // tab1
        assertThat(items.get(0), instanceOf(TabsRegion.Tab.class));
        assertThat(items.get(0).getLabel(), is("Tab1"));
        List<CompiledRegionItem> content = items.get(0).getContent();
        assertThat(content.size(), is(3));
        // tab1 form
        assertThat(content.get(0), instanceOf(Form.class));
        assertThat(((Form) content.get(0)).getId(), is("testTabsRegion_tab1"));
        // tab1 tabs
        assertThat(content.get(1), instanceOf(TabsRegion.class));
        List<TabsRegion.Tab> tabsItems = ((TabsRegion) content.get(1)).getItems();
        assertThat(tabsItems.size(), is(1));
        // tab1 tabs tab2
        TabsRegion.Tab tab2 = tabsItems.get(0);
        assertThat(tab2, instanceOf(TabsRegion.Tab.class));
        assertThat(tab2.getLabel(), is("Tab2"));
        assertThat(tab2.getDatasource(), is("testTabsRegion_ds1"));
        assertThat(tab2.getConditions().get(ValidationTypeEnum.ENABLED).get(0).getExpression(), is("id!=1"));
        assertThat(tab2.getConditions().get(ValidationTypeEnum.ENABLED).get(0).getModelLink(), is("models.resolve['testTabsRegion_ds1']"));
        assertThat(tab2.getConditions().get(ValidationTypeEnum.VISIBLE).get(0).getExpression(), is("id!=1"));
        assertThat(tab2.getConditions().get(ValidationTypeEnum.VISIBLE).get(0).getModelLink(), is("models.resolve['testTabsRegion_ds1']"));
        // tab1 tabs tab2 form
        assertThat(tabsItems.get(0).getContent().size(), is(1));
        assertThat(tabsItems.get(0).getContent().get(0), instanceOf(Form.class));
        assertThat(((Form) tabsItems.get(0).getContent().get(0)).getId(), is("testTabsRegion_tab2"));
    }

    private static void checkTabs2(Region region) {
        assertThat(region, allOf(
                instanceOf(TabsRegion.class),
                hasProperty("id", is("testTabsRegion_tabs4")),
                hasProperty("alwaysRefresh", is(true)),
                hasProperty("lazy", is(false)),
                hasProperty("hideSingleTab", is(true)),
                hasProperty("scrollbar", is(true)),
                hasProperty("maxHeight", is("300px")),
                hasProperty("datasource", is("testTabsRegion_ds")),
                hasProperty("activeTabFieldId", is("activeTab")),
                hasProperty("activeParam", is("param1"))
        ));
        TabsRegion tabsRegion = (TabsRegion) region;
        assertThat(tabsRegion.getItems().size(), is(1));
        assertThat(tabsRegion.getItems().get(0).getId(), is("testTabsRegion_tab5"));
        List<CompiledRegionItem> content = tabsRegion.getItems().get(0).getContent();
        assertThat(content.size(), is(2));
        assertThat(content.get(0), instanceOf(Table.class));
        assertThat(((Table<?>) content.get(0)).getId(), is("testTabsRegion_tab4"));
        assertThat(content.get(1), instanceOf(Table.class));
        assertThat(((Table<?>) content.get(1)).getId(), is("testTabsRegion_tab5"));
    }

    @Test
    void testTabsRegionRoute() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegionRoute.page.xml")
                .get(new PageContext("testTabsRegionRoute"));

        Map<String, PageRoutes.Query> queryMapping = page.getRoutes().getQueryMapping();
        assertThat(queryMapping.containsKey("testTabsRegion_tabs0"), is(false));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("testTabsRegionRoute_tabs0").getOnGet().getPayload()).getRegionId(), is("testTabsRegionRoute_tabs0"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("testTabsRegionRoute_tabs0").getOnGet().getPayload()).getActiveEntity(), is(":testTabsRegionRoute_tabs0"));
        assertThat(queryMapping.get("testTabsRegionRoute_tabs0").getOnSet().getLink(), is("regions.testTabsRegionRoute_tabs0.activeEntity"));
        assertThat(queryMapping.containsKey("param1"), is(true));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param1").getOnGet().getPayload()).getRegionId(), is("testTabsRegionRoute_tabs4"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param1").getOnGet().getPayload()).getActiveEntity(), is(":param1"));
        assertThat(queryMapping.get("param1").getOnSet().getLink(), is("regions.testTabsRegionRoute_tabs4.activeEntity"));
        assertThat(queryMapping.containsKey("param2"), is(true));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param2").getOnGet().getPayload()).getRegionId(), is("tabId"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param2").getOnGet().getPayload()).getActiveEntity(), is(":param2"));
        assertThat(queryMapping.get("param2").getOnSet().getLink(), is("regions.tabId.activeEntity"));
    }

    @Test
    void testTabIdUnique() {
        //неуникальные id на одной странице
        try {
            compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegionUniqueId.page.xml")
                    .get(new PageContext("testTabsRegionUniqueId"));
            fail();
        } catch (N2oException e) {
            assertThat(e.getMessage(), is("Вкладка с идентификатором 'test' уже существует"));
        }

        //неуникальные id на родительской и открываемой странице
        try {
            PageContext pageContext = new PageContext("testTabsRegionUniqueId2");
            HashSet<String> tabIds = new HashSet<>();
            tabIds.add("test");
            pageContext.setParentTabIds(tabIds);
            compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegionUniqueId2.page.xml")
                    .get(pageContext);
            fail();
        } catch (N2oException e) {
            assertThat(e.getMessage(), is("Вкладка с идентификатором 'test' уже существует"));
        }
    }
}
