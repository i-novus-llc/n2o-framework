package net.n2oapp.framework.autotest;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class N2oSimpleAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oPagesPack(), new N2oWidgetsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/simple/test.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/simple/index.page.xml"));
    }

    @Test
    public void test() {
        SimplePage page = N2oSelenide.open("http://localhost:" + port, SimplePage.class);
        page.shouldExists();
        page.widget(FormWidget.class).shouldExists();
    }
}
