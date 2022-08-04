package net.n2oapp.framework.autotest.browser_storage;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    /**
     * Тестирование sessionStorage
     */
    @Test
    public void testSessionStorage() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/session_storage/index.page.xml"));
        StandardPage page = open(StandardPage.class);
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
        inputDef.shouldHaveValue("test");
        select.shouldBeEmpty();
        checkboxGroup.shouldBeEmpty();

        input.val("test browser-storage");
        checkboxGroup.check("Петр Сергеев");
        checkboxGroup.check("Алексей Иванов");
        select.select(1);
        inputDef.clear();
        inputDef.shouldBeEmpty();

        Selenide.refresh();

        input.shouldHaveValue("test browser-storage");
        inputDef.shouldBeEmpty();
        select.shouldSelected("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");

        openNewWindow();
        input.shouldHaveValue("test browser-storage");
        inputDef.shouldBeEmpty();
        select.shouldSelected("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");

        input.val("test session-storage");
        checkboxGroup.uncheck("Петр Сергеев");
        checkboxGroup.check("Иван Алексеев");
        select.select(3);
        inputDef.val("test session-storage");
        inputDef.shouldHaveValue("test session-storage");

        Selenide.refresh();
        input.shouldHaveValue("test session-storage");
        inputDef.shouldHaveValue("test session-storage");
        select.shouldSelected("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");

        Selenide.switchTo().window(0);
        Selenide.refresh();
        input.shouldHaveValue("test browser-storage");
        inputDef.shouldBeEmpty();
        select.shouldSelected("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");
    }


    /**
     * Тестирование localStorage
     */
    @Test
    public void testLocalStorage() {

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/browser_storage/local_storage/index.page.xml"));
        StandardPage page = open(StandardPage.class);
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
        inputDef.shouldHaveValue("test");
        select.shouldBeEmpty();
        checkboxGroup.shouldBeEmpty();

        input.val("test browser-storage");
        checkboxGroup.check("Петр Сергеев");
        checkboxGroup.check("Алексей Иванов");
        select.select(1);
        inputDef.clear();
        inputDef.shouldBeEmpty();

        Selenide.refresh();

        input.shouldHaveValue("test browser-storage");
        inputDef.shouldBeEmpty();
        select.shouldSelected("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");

        openNewWindow();
        input.shouldHaveValue("test browser-storage");
        inputDef.shouldBeEmpty();
        select.shouldSelected("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");

        input.val("test local-storage");
        checkboxGroup.uncheck("Петр Сергеев");
        checkboxGroup.check("Иван Алексеев");
        select.select(3);
        inputDef.val("test local-storage");
        inputDef.shouldHaveValue("test local-storage");

        Selenide.refresh();
        input.shouldHaveValue("test local-storage");
        inputDef.shouldHaveValue("test local-storage");
        select.shouldSelected("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");

        Selenide.switchTo().window(0);
        Selenide.refresh();
        input.shouldHaveValue("test local-storage");
        inputDef.shouldHaveValue("test local-storage");
        select.shouldSelected("Петр Сергеев");
        checkboxGroup.shouldBeChecked("Иван Алексеев");
        checkboxGroup.shouldBeChecked("Алексей Иванов");
    }

    private void openNewWindow() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        String currentUrl = driver.getCurrentUrl();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.open()");
        Selenide.switchTo().window(1);
        Selenide.open(currentUrl);
    }
}
