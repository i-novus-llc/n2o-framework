package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.global.view.region.AlignEnum;
import net.n2oapp.framework.api.metadata.global.view.region.JustifyEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.*;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции страницы с тремя регионами
 */
@Deprecated(since = "7.29")
class TopLeftRightPageCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void topLeftRightPage() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testTopLeftRightPage.page.xml")
                .get(new PageContext("testTopLeftRightPage"));

        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(2));

        // top
        FlexRowRegion row = (FlexRowRegion) regions.get(0);
        checkRowAttributes(row);
        assertThat(row.getContent().size(), is(1));
        assertThat(row.getContent().get(0), instanceOf(CustomRegion.class));
        CustomRegion col = (CustomRegion) row.getContent().get(0);
        assertThat(col.getStyle().get("width"), is("100%"));
        assertThat(col.getContent().get(0), instanceOf(Form.class));
        assertThat(col.getContent().get(1), instanceOf(Table.class));
        assertThat(col.getContent().get(2), instanceOf(PanelRegion.class));
        assertThat(col.getContent().get(3), instanceOf(TabsRegion.class));

        // left-right
        row = (FlexRowRegion) regions.get(1);
        checkRowAttributes(row);
        assertThat(row.getContent().size(), is(2));

        // left
        assertThat(row.getContent().get(0), instanceOf(CustomRegion.class));
        col = (CustomRegion) row.getContent().get(0);
        assertThat(col.getContent().size(), is(3));
        assertThat(col.getStyle().get("width"), is("70%"));
        assertThat(col.getContent().get(0), instanceOf(Form.class));
        assertThat(col.getContent().get(1), instanceOf(Table.class));
        assertThat(col.getContent().get(2), instanceOf(LineRegion.class));

        // right
        assertThat(row.getContent().get(1), instanceOf(CustomRegion.class));
        col = (CustomRegion) row.getContent().get(1);
        assertThat(col.getContent().size(), is(5));
        assertThat(col.getStyle().get("width"), is("30%"));
        assertThat(col.getContent().get(0), instanceOf(PanelRegion.class));
        assertThat(col.getContent().get(1), instanceOf(TabsRegion.class));
        assertThat(col.getContent().get(2), instanceOf(Region.class));
        assertThat(col.getContent().get(3), instanceOf(Form.class));
        assertThat(col.getContent().get(4), instanceOf(Table.class));
    }

    private static void checkRowAttributes(FlexRowRegion row) {
        assertThat(row.getWrap(), is(true));
        assertThat(row.getAlign(), is(AlignEnum.TOP));
        assertThat(row.getJustify(), is(JustifyEnum.START));
    }

    @Deprecated
    @Test
    void testWidth() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testTopLeftRightPage1.page.xml")
                .get(new PageContext("testTopLeftRightPage1"));

        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(2));

        // top
        FlexRowRegion row = (FlexRowRegion) regions.get(0);
        assertThat(row.getContent().get(0), instanceOf(CustomRegion.class));
        CustomRegion col = (CustomRegion) row.getContent().get(0);
        assertThat(col.getContent().get(0), instanceOf(Form.class));
        assertThat(col.getStyle().get("width"), is("500px"));

        // left-right
        row = (FlexRowRegion) regions.get(1);
        assertThat(row.getContent().size(), is(2));

        // left
        assertThat(row.getContent().get(0), instanceOf(CustomRegion.class));
        col = (CustomRegion) row.getContent().get(0);
        assertThat(col.getStyle().get("width"), is("200px"));
        assertThat(col.getContent().size(), is(1));
        assertThat(col.getContent().get(0), instanceOf(Form.class));

        // right
        assertThat(row.getContent().get(1), instanceOf(CustomRegion.class));
        col = (CustomRegion) row.getContent().get(1);
        assertThat(col.getStyle().get("width"), is("300px"));
        assertThat(col.getContent().size(), is(1));
        assertThat(col.getContent().get(0), instanceOf(Form.class));
    }
}