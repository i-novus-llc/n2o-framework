package net.n2oapp.framework.autotest.page;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.LineRegion;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для региона с горизонтальным делителем
 */
public class LineRegionAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/simple/test.header.xml"));
    }

    @Test
    public void testLineRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/line/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        LineRegion line1 = page.place("single").region(0, LineRegion.class);
        // TODO - check title
        line1.shouldBeCollapsible();
        // TODO - убрать (должно быть open по умолчанию)
        line1.expandContent();
        line1.shouldBeExpanded();
        line1.collapseContent();
        line1.shouldBeCollapsed();
        line1.expandContent();
        line1.shouldBeExpanded();

        LineRegion line2 = page.place("single").region(1, LineRegion.class);
        // TODO - check title
        line2.shouldBeCollapsed();

        // not collapsible line
        LineRegion line3 = page.place("single").region(2, LineRegion.class);
        line3.shouldNotBeCollapsible();
    }
}
