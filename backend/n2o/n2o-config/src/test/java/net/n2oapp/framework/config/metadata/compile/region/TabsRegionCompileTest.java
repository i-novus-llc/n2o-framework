package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.action.SetActiveRegionEntityPayload;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.PanelRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
    public void testNesting() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testTabsRegionNesting.page.xml")
                .get(new PageContext("testTabsRegionNesting"));

        Assert.assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        Assert.assertThat(regions.size(), is(3));

        // PANEL1
        Assert.assertThat(regions.get(0), instanceOf(PanelRegion.class));
        Assert.assertThat(regions.get(0).getId(), is("panel_0"));
        Assert.assertThat(regions.get(0).getSrc(), is("PanelRegion"));
        Assert.assertThat(((PanelRegion) regions.get(0)).getHeaderTitle(), is("Panel1"));
        Assert.assertThat(((PanelRegion) regions.get(0)).getCollapsible(), is(false));
        Assert.assertThat(regions.get(0).getItems(), nullValue());
        List<Compiled> content = regions.get(0).getContent();
        Assert.assertThat(content.size(), is(3));
        // panel form1
        Assert.assertThat(content.get(0), instanceOf(Form.class));
        Assert.assertThat(((Form) content.get(0)).getId(), is("testPanelRegionNesting_panel1"));
        Assert.assertThat(((Form) content.get(0)).getName(), is("form1"));
        // panel panel
        Assert.assertThat(content.get(1), instanceOf(PanelRegion.class));
        Assert.assertThat(((PanelRegion) content.get(1)).getId(), is("panel_1"));
        Assert.assertThat(((PanelRegion) content.get(1)).getCollapsible(), is(true));
        Assert.assertThat(((PanelRegion) content.get(1)).getContent().size(), is(2));
        List<Compiled> panel1Content = ((PanelRegion) content.get(1)).getContent();
        Assert.assertThat(panel1Content.size(), is(2));
        // panel panel form2
        Assert.assertThat(panel1Content.get(0), instanceOf(Form.class));
        Assert.assertThat(((Form) panel1Content.get(0)).getId(), is("testPanelRegionNesting_panel2"));
        Assert.assertThat(((Form) panel1Content.get(0)).getName(), is("form2"));
        Assert.assertThat(((Form) panel1Content.get(0)).getRoute(), is("/form2"));
        // panel panel panel
        Assert.assertThat(panel1Content.get(1), instanceOf(PanelRegion.class));
        Assert.assertThat(((PanelRegion) panel1Content.get(1)).getId(), is("panel_2"));
        List<Compiled> panel2Content = ((PanelRegion) panel1Content.get(1)).getContent();
        Assert.assertThat(panel2Content.size(), is(1));
        // panel panel panel form3
        Assert.assertThat(panel2Content.get(0), instanceOf(Form.class));
        Assert.assertThat(((Form) panel2Content.get(0)).getId(), is("testPanelRegionNesting_panel3"));
        Assert.assertThat(((Form) panel2Content.get(0)).getName(), is("form3"));
        Assert.assertThat(((Form) panel2Content.get(0)).getRoute(), is("/panel3"));
        // panel form4
        Assert.assertThat(content.get(2), instanceOf(Form.class));
        Assert.assertThat(((Form) content.get(2)).getId(), is("testPanelRegionNesting_panel4"));
        Assert.assertThat(((Form) content.get(2)).getName(), is("form4"));

        // PANEL2
        Assert.assertThat(regions.get(1), instanceOf(PanelRegion.class));
        Assert.assertThat(regions.get(1).getId(), is("panel_3"));
        Assert.assertThat(regions.get(1).getSrc(), is("PanelRegion"));
        Assert.assertThat(regions.get(1).getItems(), nullValue());
        content = regions.get(1).getContent();
        Assert.assertThat(content.size(), is(2));
        // panel table1
        Assert.assertThat(content.get(0), instanceOf(Table.class));
        Assert.assertThat(((Table) content.get(0)).getId(), is("testPanelRegionNesting_panel5"));
        Assert.assertThat(((Table) content.get(0)).getName(), is("table1"));
        Assert.assertThat(((Table) content.get(0)).getRoute(), is("/panel5"));
        // panel table2
        Assert.assertThat(content.get(1), instanceOf(Table.class));
        Assert.assertThat(((Table) content.get(1)).getId(), is("testPanelRegionNesting_panel6"));
        Assert.assertThat(((Table) content.get(1)).getName(), is("table2"));
        Assert.assertThat(((Table) content.get(1)).getRoute(), is("/panel6"));

        // PANEL3
        Assert.assertThat(regions.get(2), instanceOf(PanelRegion.class));
        Assert.assertThat(regions.get(2).getId(), is("panel_4"));
        Assert.assertThat(regions.get(2).getContent(), nullValue());
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
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param1").getOnGet().getPayload()).getRegionId(), is("left_tab_1"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param1").getOnGet().getPayload()).getActiveEntity(), is(":param1"));
        assertThat(queryMapping.get("param1").getOnSet().getBindLink(), is("regions.left_tab_1.activeEntity"));
        assertThat(queryMapping.containsKey("param2"), is(true));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param2").getOnGet().getPayload()).getRegionId(), is("tabId"));
        assertThat(((SetActiveRegionEntityPayload) queryMapping.get("param2").getOnGet().getPayload()).getActiveEntity(), is(":param2"));
        assertThat(queryMapping.get("param2").getOnSet().getBindLink(), is("regions.tabId.activeEntity"));
    }
}
