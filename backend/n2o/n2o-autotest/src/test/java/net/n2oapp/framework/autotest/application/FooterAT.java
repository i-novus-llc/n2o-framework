package net.n2oapp.framework.autotest.application;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест подвала сайта
 */
class FooterAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack());

        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/application/footer/test.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/application/footer/index.page.xml"));
    }

    @Test
    void testFooter() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.footer().leftTextShouldHaveValue("left text");
        page.footer().rightTextShouldHaveValue("right text");
    }
}
