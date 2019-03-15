package net.n2oapp.framework.config.metadata.compile.region;

import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.region.PanelRegion;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PanelRegionCompileTest extends SourceCompileTestBase {

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
    public void testPanelRegion() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/page/testPanelRegion.page.xml")
                .get(new PageContext("testPanelRegion"));

        //Если в панели один виджет, то условие видимости виджета перемещается на панель
        assertThat(((PanelRegion) page.getLayout().getRegions()
                .get("single").get(0)).getDependency().getVisible().get(0).getCondition(), is("a != b"));

        //Если в панели более одного виджета, то условие видимости виджета перемещается на итемы
        assertThat(((PanelRegion) page.getLayout().getRegions()
                .get("single").get(1)).getItems().get(0).getDependency().getVisible().get(0).getCondition(), is("b != c"));

        assertThat(((PanelRegion) page.getLayout().getRegions()
                .get("single").get(1)).getItems().get(1).getDependency().getVisible().get(0).getCondition(), is("c != d"));
    }
}