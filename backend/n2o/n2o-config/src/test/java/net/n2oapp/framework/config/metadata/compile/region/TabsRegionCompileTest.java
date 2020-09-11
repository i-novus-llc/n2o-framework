package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.action.SetActiveRegionEntityPayload;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
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
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции региона в виде вкладок
 */
public class TabsRegionCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
    }

    @Test
    public void testTabsRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegion.page.xml")
                .get(new PageContext("testTabsRegion"));

        TabsRegion tabs = (TabsRegion) page.getRegions().get("single").get(0);
        assertThat(tabs.getSrc(), is("TabsRegion"));
        assertThat(tabs.getAlwaysRefresh(), is(false));
        assertThat(tabs.getLazy(), is(true));

        tabs = (TabsRegion) page.getRegions().get("left").get(0);
        assertThat(tabs.getAlwaysRefresh(), is(true));
        assertThat(tabs.getLazy(), is(false));
    }

    @Test
    public void testNesting() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegionNesting.page.xml")
                .get(new PageContext("testTabsRegionNesting"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(3));

        // TABS1
        assertThat(regions.get(0), instanceOf(TabsRegion.class));
        assertThat(regions.get(0).getId(), is("tab_0"));
        assertThat(regions.get(0).getSrc(), is("TabsRegion"));
        List<? extends Region.Item> items = ((TabsRegion) regions.get(0)).getItems();
        assertThat(items.size(), is(1));
        // tab1
        assertThat(items.get(0), instanceOf(TabsRegion.Tab.class));
        assertThat(items.get(0).getLabel(), is("Tab1"));
        List<Compiled> content = items.get(0).getContent();
        assertThat(content.size(), is(3));
        // tab1 form
        assertThat(content.get(0), instanceOf(Form.class));
        assertThat(((Form) content.get(0)).getId(), is("testTabsRegionNesting_tab1"));
        assertThat(((Form) content.get(0)).getName(), is("form1"));
        // tab1 tabs
        assertThat(content.get(1), instanceOf(TabsRegion.class));
        List<? extends Region.Item> tabsItems = ((TabsRegion) content.get(1)).getItems();
        assertThat(tabsItems.size(), is(1));
        // tab1 tabs tab2
        assertThat(tabsItems.get(0), instanceOf(TabsRegion.Tab.class));
        assertThat(tabsItems.get(0).getLabel(), is("Tab2"));
        // tab1 tabs tab2 form
        assertThat(tabsItems.get(0).getContent().size(), is(1));
        assertThat(tabsItems.get(0).getContent().get(0), instanceOf(Form.class));
        assertThat(((Form) tabsItems.get(0).getContent().get(0)).getId(), is("testTabsRegionNesting_tab2"));
        assertThat(((Form) tabsItems.get(0).getContent().get(0)).getName(), is("form2"));

        // TABS2
        TabsRegion region = (TabsRegion) regions.get(1);
        assertThat(region.getId(), is("tab_4"));
        assertThat(region.getItems().size(), is(1));
        assertThat(region.getItems().get(0).getId(), is("tab_5"));
        content = region.getItems().get(0).getContent();
        assertThat(content.size(), is(2));
        assertThat(content.get(0), instanceOf(Table.class));
        assertThat(((Table) content.get(0)).getId(), is("testTabsRegionNesting_tab4"));
        assertThat(((Table) content.get(0)).getName(), is("table1"));
        assertThat(content.get(1), instanceOf(Table.class));
        assertThat(((Table) content.get(1)).getId(), is("testTabsRegionNesting_tab5"));
        assertThat(((Table) content.get(1)).getName(), is("table2"));

        // TABS3
        region = (TabsRegion) regions.get(2);
        assertThat(region.getId(), is("tab_6"));
        assertThat(region.getItems().size(), is(1));
        assertThat(region.getItems().get(0).getId(), is("tab_7"));
        assertThat(region.getItems().get(0).getContent().size(), is(0));
    }

    @Test
    public void testV1() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegionV1.page.xml")
                .get(new PageContext("testTabsRegionV1"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(1));
        List<? extends Region.Item> items = ((TabsRegion) regions.get(0)).getItems();

        assertThat(items.size(), is(2));
        assertThat(items.get(0), instanceOf(TabsRegion.Tab.class));
        assertThat(items.get(0).getId(), is("tab_1"));
        assertThat(items.get(0).getLabel(), is("form1"));
        assertThat(items.get(0).getContent().size(), is(1));
        assertThat(items.get(0).getContent().get(0), instanceOf(Form.class));
        assertThat(((Form) items.get(0).getContent().get(0)).getName(), is("form1"));

        assertThat(items.get(1), instanceOf(TabsRegion.Tab.class));
        assertThat(items.get(1).getId(), is("tab_2"));
        assertThat(items.get(1).getLabel(), is("form2"));
        assertThat(items.get(1).getContent().size(), is(1));
        assertThat(items.get(1).getContent().get(0), instanceOf(Form.class));
        assertThat(((Form) items.get(1).getContent().get(0)).getName(), is("form2"));
    }

    @Test
    public void testTabsRegionRoute() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegion.page.xml")
                .get(new PageContext("testTabsRegion"));

        Map<String, PageRoutes.Query> queryMapping = page.getRoutes().getQueryMapping();
        assertThat(queryMapping.containsKey("tab_0"), is(true));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("tab_0").getOnGet().getPayload()).getRegionId(), is("tab_0"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("tab_0").getOnGet().getPayload()).getActiveEntity(), is(":tab_0"));
        assertThat(queryMapping.get("tab_0").getOnSet().getBindLink(), is("regions.tab_0.activeEntity"));
        assertThat(queryMapping.containsKey("param1"), is(true));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param1").getOnGet().getPayload()).getRegionId(), is("left_tab_2"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param1").getOnGet().getPayload()).getActiveEntity(), is(":param1"));
        assertThat(queryMapping.get("param1").getOnSet().getBindLink(), is("regions.left_tab_2.activeEntity"));
        assertThat(queryMapping.containsKey("param2"), is(true));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param2").getOnGet().getPayload()).getRegionId(), is("tabId"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param2").getOnGet().getPayload()).getActiveEntity(), is(":param2"));
        assertThat(queryMapping.get("param2").getOnSet().getBindLink(), is("regions.tabId.activeEntity"));
    }
}
