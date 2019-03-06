package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CustomRegionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack()
        );
    }

    @Test
    public void testCustomRegion() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/page/testCustomRegion.page.xml")
                .get(new PageContext("testCustomRegion"));

        //Условие видимости виджета должно перемещаться на итем вне зависимости от количества виджетов в регионе
        assertThat(page.getLayout().getRegions().get("single").get(0).getItems().get(0)
                .getDependency().getVisible().get(0).getCondition(), is("a != b"));
        assertThat(page.getLayout().getRegions().get("single").get(1).getItems().get(0)
                .getDependency().getVisible().get(0).getCondition(), is("b != c"));
        assertThat(page.getLayout().getRegions().get("single").get(1).getItems().get(1)
                .getDependency().getVisible().get(0).getCondition(), is("c != d"));
    }
}