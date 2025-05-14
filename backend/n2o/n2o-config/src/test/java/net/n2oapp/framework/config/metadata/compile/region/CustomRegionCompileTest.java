package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.CustomRegion;
import net.n2oapp.framework.api.metadata.meta.region.Region;
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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции кастомного региона
 */
class CustomRegionCompileTest extends SourceCompileTestBase {

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
    void testNesting() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/region/testCustomRegionNesting.page.xml")
                .get(new PageContext("testCustomRegionNesting"));

        assertThat(page.getRegions().size(), is(1));
        List<Region> regions = page.getRegions().get("single");
        assertThat(regions.size(), is(3));

        // REGION1
        assertThat(regions.get(0), instanceOf(CustomRegion.class));
        assertThat(regions.get(0).getId(), is("region0"));
        assertThat(regions.get(0).getSrc(), is("NoneRegion"));
        List<CompiledRegionItem> content = regions.get(0).getContent();
        assertThat(content.size(), is(2));
        // region form1
        assertThat(content.get(0), instanceOf(Form.class));
        assertThat(((Form) content.get(0)).getId(), is("testCustomRegionNesting_w1"));
        // region form2
        assertThat(content.get(1), instanceOf(Form.class));
        assertThat(((Form) content.get(1)).getId(), is("testCustomRegionNesting_w2"));

        // REGION2
        assertThat(regions.get(1), instanceOf(CustomRegion.class));
        assertThat(regions.get(1).getId(), is("region1"));
        assertThat(regions.get(1).getSrc(), is("NoneRegion"));
        content = regions.get(1).getContent();
        assertThat(content.size(), is(3));
        // region form3
        assertThat(content.get(0), instanceOf(Form.class));
        assertThat(((Form) content.get(0)).getId(), is("testCustomRegionNesting_w3"));

        //region region
        assertThat(content.get(1), instanceOf(CustomRegion.class));
        assertThat(((CustomRegion) content.get(1)).getId(), is("region2"));
        assertThat(((CustomRegion) content.get(1)).getSrc(), is("NoneRegion"));
        List<CompiledRegionItem> regionContent = ((CustomRegion) content.get(1)).getContent();
        assertThat(regionContent.size(), is(1));
        // region region form4
        assertThat(regionContent.get(0), instanceOf(Form.class));
        assertThat(((Form) regionContent.get(0)).getId(), is("testCustomRegionNesting_w4"));

        // region form5
        assertThat(content.get(2), instanceOf(Form.class));
        assertThat(((Form) content.get(2)).getId(), is("testCustomRegionNesting_w5"));

        // REGION3
        assertThat(regions.get(2), instanceOf(CustomRegion.class));
        assertThat(regions.get(2).getId(), is("region3"));
        assertThat(regions.get(2).getSrc(), is("NoneRegion"));
        content = regions.get(2).getContent();
        assertThat(content.size(), is(2));
        // region form6
        assertThat(content.get(0), instanceOf(Form.class));
        assertThat(((Form) content.get(0)).getId(), is("testCustomRegionNesting_w6"));
        // region form7
        assertThat(content.get(1), instanceOf(Form.class));
        assertThat(((Form) content.get(1)).getId(), is("testCustomRegionNesting_w7"));
    }
}