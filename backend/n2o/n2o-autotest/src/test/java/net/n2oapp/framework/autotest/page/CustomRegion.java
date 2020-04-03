package net.n2oapp.framework.autotest.page;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
import net.n2oapp.framework.autotest.impl.component.widget.N2oFormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomRegion extends AutoTestBase {
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/custom/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/simple/test.header.xml"));
    }

    @Test
    public void testCustomRegion() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.place("single").region(0, N2oSimpleRegion.class).content().widget(N2oFormWidget.class).element().should(Condition.exist);
    }
}
