package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.SubPageRegion;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование связывания региона `<sub-page>` с данными
 */
class SubPageRegionBinderTest extends SourceCompileTestBase {
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
    void testSubPage() {
        ReadCompileBindTerminalPipeline pipeline =
                bind("net/n2oapp/framework/config/metadata/compile/region/testSubPageRegionBinder.page.xml");
        PageContext context = new PageContext("testSubPageRegionBinder", "/user/:parentId");
        SubPageRegion region = ((SubPageRegion) ((StandardPage) pipeline.get(context, new DataSet("parentId", 2)))
                .getRegions().get("single").get(0));
        assertThat(region.getPages().get(0).getUrl(), is("/user/2/subpage1"));
    }
}
