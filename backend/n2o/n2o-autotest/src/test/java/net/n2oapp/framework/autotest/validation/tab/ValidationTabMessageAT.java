package net.n2oapp.framework.autotest.validation.tab;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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

/**
 * Автотест валидации полей в скрытых регионах
 */
class ValidationTabMessageAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        ScriptProcessor.getScriptEngine();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/validation/tab/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/tab/form.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/tab/test.object.xml")
        );
    }

    @Test
    void testValidationTabMessageInModal() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().crumb(0).shouldHaveLabel("Подсветка невалидных полей в неактивных вкладках");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        StandardButton openModalBtn = form.toolbar().bottomLeft().button("Модальное окно");
        openModalBtn.shouldBeEnabled();
        openModalBtn.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание записи");
        StandardPage newPage = modal.content(StandardPage.class);
        StandardButton saveBtn = modal.toolbar().bottomRight().button("Сохранить");
        StandardButton validationBtn = modal.toolbar().bottomRight().button("Валидировать страницу");

        validationBtn.click();

        extracted(page, newPage, saveBtn);
    }

    @Test
    void testValidationTabMessageInPage() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().crumb(0).shouldHaveLabel("Подсветка невалидных полей в неактивных вкладках");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        StandardButton openPageBtn = form.toolbar().bottomLeft().button("Новая страница");
        openPageBtn.shouldBeEnabled();
        openPageBtn.click();

        StandardPage newPage = N2oSelenide.page(StandardPage.class);
        page.breadcrumb().shouldHaveSize(2);
        page.breadcrumb().crumb(1).shouldHaveLabel("Создание записи");

        StandardButton saveBtn = newPage.toolbar().bottomRight().button("Сохранить");
        StandardButton validationBtn = newPage.toolbar().bottomRight().button("Валидировать страницу");

        validationBtn.click();

        extracted(page, newPage, saveBtn);
    }

    private void extracted(StandardPage page, StandardPage newPage, StandardButton saveBtn) {
        TabsRegion tabs = newPage.regions().region(1, TabsRegion.class);

        tabs.tab(1).shouldBeInvalid();
        tabs.tab(2).shouldBeInvalid();

        StandardField nameField = newPage.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(FormWidget.class)
                .fields()
                .field("Имя");
        InputText nameInputText = nameField.control(InputText.class);

        nameInputText.click();
        nameInputText.setValue("test");

        tabs.tab(1).shouldBeInvalid();
        tabs.tab(2).shouldBeInvalid();

        nameInputText.clear();
        nameField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));

        nameInputText.click();
        nameInputText.setValue("unique");
        nameField.shouldHaveValidationMessage(Condition.empty);

        tabs.tab(1).click();
        tabs.tab(1).shouldBeActive();

        // check tabs switch
        tabs.tab(0).click();
        tabs.tab(0).shouldBeActive();
        tabs.tab(1).click();

        StandardField orgField = tabs.tab(1)
                .content()
                .widget(FormWidget.class)
                .fields()
                .field("Название организации");
        InputText orgInputText = orgField.control(InputText.class);

        orgInputText.click();
        orgInputText.setValue("test");
        orgInputText.clear();
        orgField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));

        orgInputText.click();
        orgInputText.setValue("test");
        orgField.shouldHaveValidationMessage(Condition.text("Организация test уже существует"));

        tabs.tab(0).click();
        saveBtn.click();
        tabs.tab(0).shouldBeValid();
        tabs.tab(1).shouldBeInvalid();

        tabs.tab(1).click();
        orgField.shouldHaveValidationMessage(Condition.text("Организация test уже существует"));
        orgInputText.click();
        orgInputText.setValue("unique");
        orgField.shouldHaveValidationMessage(Condition.empty);

        tabs.tab(2).click();
        StandardField comField = tabs.tab(1)
                .content()
                .widget(FormWidget.class)
                .fields()
                .field("Отдел");
        InputText comInputText = comField.control(InputText.class);

        comField.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));

        comInputText.click();
        comInputText.setValue("test");

        comField.shouldHaveValidationMessage(Condition.empty);

        tabs.tab(0).shouldBeValid();
        tabs.tab(1).shouldBeValid();
        tabs.tab(2).shouldBeValid();

        saveBtn.click();
        page.alerts(Alert.PlacementEnum.top).alert(0).shouldHaveText("Данные сохранены");
    }
}
