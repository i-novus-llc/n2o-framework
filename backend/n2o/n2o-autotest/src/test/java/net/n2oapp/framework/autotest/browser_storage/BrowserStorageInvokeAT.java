package net.n2oapp.framework.autotest.browser_storage;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrowserStorageInvokeAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    /**
     * Тестирование invoke и
     * clear-after-invoke у localStorage
     */
    @Test
    public void testInvokeLocal() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/local_storage/invoke/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/local_storage/invoke/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/local_storage/invoke/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText input = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("Инпут").control(InputText.class);
        Button button = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .toolbar().bottomLeft().button("Отправить");

        input.val("test");
        button.click();
        input.shouldBeEmpty();
        table.columns().rows().row(0).cell(0).textShouldHave("1");
        table.columns().rows().row(0).cell(1).textShouldHave("test");
    }

    /**
     * Тестирование invoke и
     * clear-after-invoke у sessionStorage
     */
    @Test
    public void testInvokeSession() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/session_storage/invoke/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/session_storage/invoke/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/session_storage/invoke/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText input = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("Инпут").control(InputText.class);
        Button button = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .toolbar().bottomLeft().button("Отправить");

        input.val("test");
        button.click();
        input.shouldBeEmpty();
        table.columns().rows().row(0).cell(0).textShouldHave("1");
        table.columns().rows().row(0).cell(1).textShouldHave("test");
    }
}

