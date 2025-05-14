package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции региона в виде панелей
 */
class PanelRegionCompileTest extends SourceCompileTestBase {

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
    void testPanelRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testPanelRegion.page.xml")
                .get(new PageContext("testPanelRegion"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(2));

        assertThat(regions.get(0).getActiveParam(), is("param"));
        assertThat(((PanelRegion) regions.get(0)).getCollapsible(), is(false));
        assertThat(((PanelRegion) regions.get(0)).getFooterTitle(), is("footer"));
        assertThat(((PanelRegion) regions.get(0)).getIcon(), is("fa fa-plus"));
        assertThat(((PanelRegion) regions.get(0)).getColor(), is("danger"));
        assertThat(((PanelRegion) regions.get(0)).getOpen(), is(false));
        assertThat(((PanelRegion) regions.get(0)).getHeader(), is(false));

        assertThat(((PanelRegion) regions.get(1)).getCollapsible(), is(true));
        assertThat(((PanelRegion) regions.get(1)).getOpen(), is(true));
        assertThat(((PanelRegion) regions.get(1)).getFullScreen(), is(false));
        assertThat(((PanelRegion) regions.get(1)).getHeader(), is(true));
    }

    @Test
    void testNesting() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testPanelRegionNesting.page.xml")
                .get(new PageContext("testPanelRegionNesting"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(3));

        // PANEL1
        assertThat(regions.get(0), instanceOf(PanelRegion.class));
        assertThat(regions.get(0).getId(), is("panel0"));
        assertThat(regions.get(0).getSrc(), is("PanelRegion"));
        assertThat(((PanelRegion) regions.get(0)).getHeaderTitle(), is("Panel1"));
        assertThat(((PanelRegion) regions.get(0)).getCollapsible(), is(false));
        List<CompiledRegionItem> content = regions.get(0).getContent();
        assertThat(content.size(), is(3));
        // panel form1
        assertThat(content.get(0), instanceOf(Form.class));
        assertThat(((Form) content.get(0)).getId(), is("testPanelRegionNesting_panel1"));
        // panel panel
        assertThat(content.get(1), instanceOf(PanelRegion.class));
        assertThat(((PanelRegion) content.get(1)).getId(), is("panel1"));
        assertThat(((PanelRegion) content.get(1)).getCollapsible(), is(true));
        assertThat(((PanelRegion) content.get(1)).getContent().size(), is(2));
        List<CompiledRegionItem> panel1Content = ((PanelRegion) content.get(1)).getContent();
        assertThat(panel1Content.size(), is(2));
        // panel panel form2
        assertThat(panel1Content.get(0), instanceOf(Form.class));
        assertThat(((Form) panel1Content.get(0)).getId(), is("testPanelRegionNesting_panel2"));
        // panel panel panel
        assertThat(panel1Content.get(1), instanceOf(PanelRegion.class));
        assertThat(((PanelRegion) panel1Content.get(1)).getId(), is("panel2"));
        List<CompiledRegionItem> panel2Content = ((PanelRegion) panel1Content.get(1)).getContent();
        assertThat(panel2Content.size(), is(1));
        // panel panel panel form3
        assertThat(panel2Content.get(0), instanceOf(Form.class));
        assertThat(((Form) panel2Content.get(0)).getId(), is("testPanelRegionNesting_panel3"));
        // panel form4
        assertThat(content.get(2), instanceOf(Form.class));
        assertThat(((Form) content.get(2)).getId(), is("testPanelRegionNesting_panel4"));

        // PANEL2
        assertThat(regions.get(1), instanceOf(PanelRegion.class));
        assertThat(regions.get(1).getId(), is("panel3"));
        assertThat(regions.get(1).getSrc(), is("PanelRegion"));
        content = regions.get(1).getContent();
        assertThat(content.size(), is(2));
        // panel table1
        assertThat(content.get(0), instanceOf(Table.class));
        assertThat(((Table) content.get(0)).getId(), is("testPanelRegionNesting_panel5"));
        // panel table2
        assertThat(content.get(1), instanceOf(Table.class));
        assertThat(((Table) content.get(1)).getId(), is("testPanelRegionNesting_panel6"));

        // PANEL3
        assertThat(regions.get(2), instanceOf(PanelRegion.class));
        assertThat(regions.get(2).getId(), is("panel4"));
        assertThat(regions.get(2).getContent(), nullValue());
    }
}