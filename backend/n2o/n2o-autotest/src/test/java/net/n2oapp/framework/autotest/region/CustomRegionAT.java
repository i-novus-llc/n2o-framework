package net.n2oapp.framework.autotest.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.*;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для простого региона
 */
public class CustomRegionAT extends AutoTestBase {
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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/simple/test.application.xml"));
    }

    @Test
    public void testCustomRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/custom/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).element().should(Condition.exist);
    }

    @Test
    public void testContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/custom/nesting/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        // widgets in <regions> should be contained in SimpleRegion
        SimpleRegion simpleRegion = page.regions().region(0, SimpleRegion.class);
        RegionItems content = simpleRegion.content();
        content.widget(0, FormWidget.class).fields().field("field1").shouldExists();
        content.widget(1, FormWidget.class).fields().field("field2").shouldExists();

        SimpleRegion simpleRegion2 = page.regions().region(1, SimpleRegion.class);
        content = simpleRegion2.content();
        content.widget(0, FormWidget.class).fields().field("field3").shouldExists();

        PanelRegion panel = content.region(1, PanelRegion.class);
        panel.shouldExists();
        panel.shouldHaveTitle("Panel");

        LineRegion line = content.region(2, LineRegion.class);
        line.shouldExists();
        line.shouldHaveLabel("Line");

        TabsRegion tabs = content.region(3, TabsRegion.class);
        tabs.shouldExists();
        tabs.shouldHaveSize(2);
        tabs.tab(1).shouldHaveName("Tab2");

        content.widget(4, FormWidget.class).fields().field("field4").shouldExists();
    }
}
