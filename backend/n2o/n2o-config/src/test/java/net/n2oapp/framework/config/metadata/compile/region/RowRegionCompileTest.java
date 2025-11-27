package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.global.view.region.AlignEnum;
import net.n2oapp.framework.api.metadata.global.view.region.JustifyEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.*;
import net.n2oapp.framework.api.metadata.meta.region.scrollspy.ScrollspyRegion;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
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
 * Тестирование компиляции региона {@code <row>}
 */
class RowRegionCompileTest extends SourceCompileTestBase {

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
    void testRowRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testRowRegion.page.xml")
                .get(new PageContext("testRowRegion"));
        List<Region> regions = page.getRegions().get("single");
        RowRegion region = ((RowRegion) regions.get(0));

        assertThat(region.getId(), is("test"));
        assertThat(region.getSrc(), is("Layout/Row"));
        assertThat(region.getColumns(), is(3));
        assertThat(region.getWrap(), is(false));
        assertThat(region.getAlign(), is(AlignEnum.MIDDLE));
        assertThat(region.getJustify(), is(JustifyEnum.CENTER));

        List<CompiledRegionItem> content = region.getContent();
        assertThat(content, notNullValue());
        assertThat(content.size(), is(3));

        // Первый элемент - регион tabs, должен быть обернут в col
        assertThat(content.get(0), instanceOf(ColRegion.class));
        ColRegion col1 = (ColRegion) content.get(0);
        assertThat(col1.getSrc(), is("Layout/Col"));
        assertThat(col1.getContent().get(0), instanceOf(TabsRegion.class));
        TabsRegion tabs = (TabsRegion) col1.getContent().get(0);
        assertThat(tabs.getItems().size(), is(1));

        // Второй элемент - виджет form, должен быть обернут в col
        assertThat(content.get(1), instanceOf(ColRegion.class));
        ColRegion col2 = (ColRegion) content.get(1);
        assertThat(col2.getSrc(), is("Layout/Col"));
        assertThat(col2.getContent().get(0), instanceOf(Form.class));
        Form form1 = (Form) col2.getContent().get(0);
        assertThat(form1.getId(), is("testRowRegion_form1"));

        // Третий элемент - регион col
        assertThat(content.get(2), instanceOf(ColRegion.class));
        ColRegion col = (ColRegion) content.get(2);
        assertThat(col.getSrc(), is("Layout/Col"));
        assertThat(col.getContent().size(), is(2));

        assertThat(col.getContent().get(0), instanceOf(ScrollspyRegion.class));
        ScrollspyRegion scrollspy = (ScrollspyRegion) col.getContent().get(0);
        assertThat(scrollspy.getId(), is("scrollspy1"));

        assertThat(col.getContent().get(1), instanceOf(Form.class));
        Form form2 = (Form) col.getContent().get(1);
        assertThat(form2.getId(), is("testRowRegion_form2"));
    }

    @Test
    void testRowRegionDefaults() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testRowRegionDefaults.page.xml")
                .get(new PageContext("testRowRegionDefaults"));
        List<Region> regions = page.getRegions().get("single");
        RowRegion region = ((RowRegion) regions.get(0));

        assertThat(region.getId(), is("row0"));
        assertThat(region.getColumns(), is(12));
        assertThat(region.getWrap(), is(true));
        assertThat(region.getAlign(), is(AlignEnum.TOP));
        assertThat(region.getJustify(), is(JustifyEnum.START));
    }
}