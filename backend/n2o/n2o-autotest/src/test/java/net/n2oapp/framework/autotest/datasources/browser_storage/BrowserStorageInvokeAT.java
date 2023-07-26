package net.n2oapp.framework.autotest.datasources.browser_storage;

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

/**
 * Тестирование invoke browser-storage
 */
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
    }

    /**
     * Тестирование invoke и
     * clear-after-invoke у localStorage
     */
    @Test
    public void testInvokeLocal() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_local_storage");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_local_storage/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_local_storage/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_local_storage/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        invokeBrowserStorage(page);
    }

    /**
     * Тестирование invoke и
     * clear-after-invoke у sessionStorage
     */
    @Test
    public void testInvokeSession() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_session_storage");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_session_storage/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_session_storage/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/invoke_session_storage/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        invokeBrowserStorage(page);
    }

    private void invokeBrowserStorage(StandardPage page) {
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText input = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("Тест").control(InputText.class);
        Button button = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .toolbar().bottomLeft().button("Отправить");

        input.click();
        input.setValue("test");
        button.click();
        input.shouldBeEmpty();
        table.columns().rows().row(0).cell(0).shouldHaveText("1");
        table.columns().rows().row(0).cell(1).shouldHaveText("test");
    }
}
