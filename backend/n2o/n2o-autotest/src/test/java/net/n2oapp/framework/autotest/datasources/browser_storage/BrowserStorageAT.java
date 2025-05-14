package net.n2oapp.framework.autotest.datasources.browser_storage;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Text;
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
class BrowserStorageAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    /**
     * Тестирование localStorage
     */
    @Test
    void testLocalStorage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/local_storage/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        testBrowserStorage(page);
    }

    /**
     * Тестирование sessionStorage
     */
    @Test
    void testSessionStorage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/session_storage/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        testBrowserStorage(page);
    }

    @Test
    void testSubmit() {
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
        select.shouldHaveValue("Введите значение");
        checkboxGroup.shouldBeEmpty();

        input.click();
        input.setValue("test browser-storage");
        checkboxGroup.check("Петр Сергеев");
        checkboxGroup.check("Алексей Иванов");
        select.openPopup();
        select.dropdown().selectItem(1);
        inputDef.click();
        inputDef.setValue("test");
        select.click();
        inputDef.click();
        inputDef.clear();
        select.click();
        inputDef.shouldBeEmpty();

        Selenide.sleep(500);
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
        select.shouldHaveValue("Введите значение");
        checkboxGroup.shouldBeEmpty();
    }

    @Test
    void testFetchOnInit() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/fetch_on_init/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/fetch_on_init/test.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        InputText input = formWidget.fields().field("Инпут").control(InputText.class);
        Button button = formWidget.toolbar().bottomLeft().button("test");
        Button submit = formWidget.toolbar().bottomLeft().button("Submit");

        input.setValue("testValue");
        submit.click();
        input.shouldHaveValue("testValue");
        button.click();

        StandardPage open = N2oSelenide.page(StandardPage.class);
        open.breadcrumb().crumb(1).shouldHaveLabel("fetch-on-init тест");
        InputText testInput = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields().field("Инпут").control(InputText.class);
        testInput.shouldHaveValue("testValue");
    }

    @Test
    void testFieldDependency() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/field_dependency/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/field_dependency/modal.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget formWidget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        Button button = formWidget.fields().field("Открыть", ButtonField.class);
        button.click();

        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        Fields fields = modalPage.content(StandardPage.class).regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields();
        InputText age = fields.field("age").control(InputText.class);
        Text text = fields.field(1, Text.class);
        Button save = modalPage.toolbar().bottomRight().button("Сохранить");

        age.shouldBeEmpty();
        text.shouldHaveText("Несовершеннолетний");

        age.setValue("14");
        text.shouldHaveText("Несовершеннолетний");

        age.setValue("19");
        text.shouldHaveText("Совершеннолетний");

        save.click();
        modalPage.shouldNotExists();

        button.click();
        modalPage.shouldExists();
        age.shouldHaveValue("19");
        text.shouldHaveText("Совершеннолетний");
    }
}
