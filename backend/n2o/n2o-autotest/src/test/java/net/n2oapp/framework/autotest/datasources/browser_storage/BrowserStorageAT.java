package net.n2oapp.framework.autotest.datasources.browser_storage;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.*;

/**
 * Тестирование browser-storage
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class BrowserStorageAT extends AutoTestBase {

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
     * Тестирование localStorage
     */
    @Test
    public void testLocalStorage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/local_storage/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        testBrowserStorage(page);
    }

    /**
     * Тестирование sessionStorage
     */
    @Test
    public void testSessionStorage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/session_storage/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        testBrowserStorage(page);
    }

    @Test
    public void testSubmit() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/submit/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        InputText input = formWidget.fields().field("test submit").control(InputText.class);
        Button button = formWidget.toolbar().bottomLeft().button("Submit");

        input.shouldBeEmpty();
        input.click();
        input.setValue("test submit");

        Selenide.refresh();

        page.shouldExists();
        formWidget.shouldExists();
        input.shouldBeEmpty();

        input.click();
        input.setValue("test submit");
        button.click();

        Selenide.refresh();

        page.shouldExists();
        formWidget.shouldExists();
        input.shouldHaveValue("test submit");

        Selenide.clearBrowserLocalStorage();
        Selenide.refresh();

        page.shouldExists();
        formWidget.shouldExists();
        input.shouldBeEmpty();
    }

    private void testBrowserStorage(StandardPage page) {
        page.shouldExists();

        InputText input = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Инпут").control(InputText.class);
        InputText inputDef = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Инпут с default value").control(InputText.class);
        Select select = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Ввод с выпадающим списком").control(Select.class);
        CheckboxGroup checkboxGroup = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Чекбоксы").control(CheckboxGroup.class);

        input.shouldBeEmpty();
        inputDef.shouldHaveValue("text");
        select.shouldBeEmpty();
        checkboxGroup.shouldBeEmpty();

        input.click();
        input.setValue("test browser-storage");
        checkboxGroup.check("Петр Сергеев");
        checkboxGroup.check("Алексей Иванов");
        select.select(1);
        inputDef.click();
        inputDef.setValue("test");
        select.click();
        inputDef.click();
        inputDef.clear();
        select.click();
        inputDef.shouldBeEmpty();

        Selenide.refresh();

        page.shouldExists();

        input.shouldHaveValue("test browser-storage");
        select.shouldSelected("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");
        inputDef.shouldBeEmpty();

        Selenide.clearBrowserLocalStorage();
        Selenide.sessionStorage().clear();
        Selenide.refresh();

        page.shouldExists();

        input.shouldBeEmpty();
        inputDef.shouldHaveValue("text");
        select.shouldBeEmpty();
        checkboxGroup.shouldBeEmpty();
    }
}
