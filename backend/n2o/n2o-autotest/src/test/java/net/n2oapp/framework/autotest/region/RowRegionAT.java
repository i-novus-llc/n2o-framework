package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.ColRegion;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;
import net.n2oapp.framework.autotest.api.component.region.RowRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Автотест для региона {@code <row>}
 */
class RowRegionAT extends AutoTestBase {
    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    void testSimple() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/row/simple/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RowRegion row = page.regions().region(0, RowRegion.class);
        row.shouldExists();
        row.shouldHaveColumns("12");
        row.shouldBeWrapped();
        row.shouldHaveJustify("start");
        row.shouldHaveAlignment("top");
        row.content().shouldHaveSize(0);

        row = page.regions().region(1, RowRegion.class);
        row.shouldExists();
        row.shouldHaveColumns("10");
        row.shouldNotBeWrapped();
        row.shouldHaveJustify("center");
        row.shouldHaveAlignment("middle");
        row.content().shouldHaveSize(2);

        ColRegion col = row.content().region(0, ColRegion.class);
        col.shouldExists();
        col.shouldHaveSize(1);
        col.shouldHaveOffset(0);
        col.content().shouldHaveSize(0);

        col = row.content().region(1, ColRegion.class);
        col.shouldExists();
        col.shouldHaveSize(2);
        col.shouldHaveOffset(3);
        col.content().shouldHaveSize(1);
    }

    @Test
    void testMixed() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/row/mixed/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RowRegion row = page.regions().region(0, RowRegion.class);
        row.content().shouldHaveSize(1);
        ColRegion col = row.content().region(0, ColRegion.class);
        PanelRegion panel = col.content().region(0, PanelRegion.class);
        panel.shouldHaveTitle("panel1");

        row = page.regions().region(1, RowRegion.class);
        row.content().shouldHaveSize(2);

        col = row.content().region(0, ColRegion.class);
        col.content().shouldHaveSize(2);
        TabsRegion tabs = col.content().region(0, TabsRegion.class);
        tabs.shouldHaveSize(3);
        FormWidget form = col.content().widget(1, FormWidget.class);
        form.fields().field(0).shouldHaveLabel("input1");

        col = row.content().region(1, ColRegion.class);
        panel = col.content().region(0, PanelRegion.class);
        panel.shouldHaveTitle("panel2");
    }
}