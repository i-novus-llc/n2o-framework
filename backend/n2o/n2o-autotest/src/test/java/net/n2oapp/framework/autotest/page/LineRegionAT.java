package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.impl.component.region.N2oLineRegion;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LineRegionAT extends AutoTestBase {
    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/line/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/simple/test.header.xml"));
    }

    @Test
    public void lineRegionTest() {
        StandardPage page = open(StandardPage.class);
        N2oLineRegion region = page.place("single").region(0, N2oLineRegion.class);
        region.shouldBeExpanded();
        N2oLineRegion collapsible = page.place("single").region(1, N2oLineRegion.class);
        collapsible.shouldBeExpanded();
        collapsible.toggleCollapse();
        collapsible.shouldBeCollapsed();
        collapsible.toggleCollapse();
        collapsible.shouldBeExpanded();
    }
}
