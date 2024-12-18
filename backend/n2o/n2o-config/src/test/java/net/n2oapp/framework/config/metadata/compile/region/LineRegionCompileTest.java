package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.LineRegion;
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
 * Тестирование компиляции региона с горизонтальным делителем
 */
class LineRegionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
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
    void testLineRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testLineRegion.page.xml")
                .get(new PageContext("testLineRegion"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(2));

        assertThat(regions.get(0).getSrc(), is("ListRegion"));
        assertThat(((LineRegion) regions.get(0)).getLabel(), is("Line1"));
        assertThat(((LineRegion) regions.get(0)).getCollapsible(), is(false));
        assertThat(((LineRegion) regions.get(0)).getExpand(), is(false));
        assertThat(((LineRegion) regions.get(0)).getHasSeparator(), is(false));
        assertThat((regions.get(0)).getClassName(), is("testClass"));
        assertThat(regions.get(0).getStyle().size(), is(2));

        assertThat(((LineRegion) regions.get(1)).getLabel(), nullValue());
        assertThat(((LineRegion) regions.get(1)).getCollapsible(), is(true));
        assertThat(((LineRegion) regions.get(1)).getExpand(), is(true));
        assertThat(((LineRegion) regions.get(1)).getHasSeparator(), is(true));
        assertThat((regions.get(1)).getClassName(), nullValue());
        assertThat((regions.get(1)).getStyle(), nullValue());

    }

    @Test
    void testNesting() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testLineRegionNesting.page.xml")
                .get(new PageContext("testLineRegionNesting"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(3));

        // LINE1
        assertThat(regions.get(0), instanceOf(LineRegion.class));
        assertThat(regions.get(0).getId(), is("line0"));
        assertThat(((LineRegion) regions.get(0)).getLabel(), is("Line1"));
        assertThat(((LineRegion) regions.get(0)).getCollapsible(), is(false));
        List<CompiledRegionItem> content = regions.get(0).getContent();
        assertThat(content.size(), is(3));
        // line form1
        assertThat(content.get(0), instanceOf(Form.class));
        assertThat(((Form) content.get(0)).getId(), is("testLineRegionNesting_line1"));
        // line line
        assertThat(content.get(1), instanceOf(LineRegion.class));
        assertThat(((LineRegion) content.get(1)).getId(), is("line1"));
        assertThat(((LineRegion) content.get(1)).getCollapsible(), is(true));
        assertThat(((LineRegion) content.get(1)).getContent().size(), is(2));
        List<CompiledRegionItem> line1Content = ((LineRegion) content.get(1)).getContent();
        assertThat(line1Content.size(), is(2));
        // line line form2
        assertThat(line1Content.get(0), instanceOf(Form.class));
        assertThat(((Form) line1Content.get(0)).getId(), is("testLineRegionNesting_line2"));
        // line line line
        assertThat(line1Content.get(1), instanceOf(LineRegion.class));
        assertThat(((LineRegion) line1Content.get(1)).getId(), is("line2"));
        List<CompiledRegionItem> line2Content = ((LineRegion) line1Content.get(1)).getContent();
        assertThat(line2Content.size(), is(1));
        // line line line form3
        assertThat(line2Content.get(0), instanceOf(Form.class));
        assertThat(((Form) line2Content.get(0)).getId(), is("testLineRegionNesting_line3"));
        // line form4
        assertThat(content.get(2), instanceOf(Form.class));
        assertThat(((Form) content.get(2)).getId(), is("testLineRegionNesting_line4"));

        // LINE2
        assertThat(regions.get(1), instanceOf(LineRegion.class));
        assertThat(regions.get(1).getId(), is("line3"));
        content = regions.get(1).getContent();
        assertThat(content.size(), is(2));
        // line table1
        assertThat(content.get(0), instanceOf(Table.class));
        assertThat(((Table) content.get(0)).getId(), is("testLineRegionNesting_line5"));
        // line table2
        assertThat(content.get(1), instanceOf(Table.class));
        assertThat(((Table) content.get(1)).getId(), is("testLineRegionNesting_line6"));

        // LINE 3
        assertThat(regions.get(2), instanceOf(LineRegion.class));
        assertThat(regions.get(2).getId(), is("line4"));
        assertThat(regions.get(2).getContent(), nullValue());
    }
}
