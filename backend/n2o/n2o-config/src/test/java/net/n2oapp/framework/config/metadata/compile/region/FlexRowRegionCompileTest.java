package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.global.view.region.AlignEnum;
import net.n2oapp.framework.api.metadata.global.view.region.JustifyEnum;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.FlexRowRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
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
 * Тестирование компиляции региона {@code <flex-row>}
 */
class FlexRowRegionCompileTest extends SourceCompileTestBase {

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
    void testFlexRowRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testFlexRowRegion.page.xml")
                .get(new PageContext("testFlexRowRegion"));
        List<Region> regions = page.getRegions().get("single");
        FlexRowRegion region = ((FlexRowRegion) regions.get(0));

        assertThat(region.getId(), is("test"));
        assertThat(region.getWrap(), is(false));
        assertThat(region.getAlign(), is(AlignEnum.STRETCH));
        assertThat(region.getJustify(), is(JustifyEnum.SPACE_BETWEEN));

        List<CompiledRegionItem> content = region.getContent();
        assertThat(content, notNullValue());
        assertThat(content.size(), is(2));

        assertThat(content.get(0), instanceOf(TabsRegion.class));
        TabsRegion tabs = (TabsRegion) content.get(0);
        assertThat(tabs.getItems().size(), is(1));

        assertThat(content.get(1), instanceOf(Form.class));
        Form form1 = (Form) content.get(1);
        assertThat(form1.getId(), is("testFlexRowRegion_form1"));
    }

    @Test
    void testFlexRowRegionDefaults() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testFlexRowRegionDefaults.page.xml")
                .get(new PageContext("testFlexRowRegionDefaults"));
        List<Region> regions = page.getRegions().get("single");
        FlexRowRegion region = ((FlexRowRegion) regions.get(0));

        assertThat(region.getId(), is("flex_row0"));
        assertThat(region.getWrap(), is(true));
        assertThat(region.getAlign(), is(AlignEnum.TOP));
        assertThat(region.getJustify(), is(JustifyEnum.START));
    }
}