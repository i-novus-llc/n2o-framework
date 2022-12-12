package net.n2oapp.framework.autotest.multi_fieldset;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MultiFieldsetAT extends AutoTestBase {
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
    }

    @Test
    public void test() throws InterruptedException {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/add.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/multi_fieldset/update.page.xml"));
        Configuration.headless=false;

        StandardPage open = open(StandardPage.class);
        Thread.sleep(5000);
    }
}
