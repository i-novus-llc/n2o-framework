package net.n2oapp.framework.autotest.validation.tab;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
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
public class ValidationTabMessageAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        ScriptProcessor.getScriptEngine();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/tab/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/tab/form.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/validation/tab/test.object.xml"));
    }

    @Test
    public void testValidationTabMessageInModal() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().titleShouldHaveText("Подсветка невалидных полей в неактивных вкладках");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        StandardButton button = form.toolbar().bottomLeft().button("Модальное окно");
        button.shouldBeEnabled();
        button.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание записи");
        button = modal.toolbar().bottomRight().button("Сохранить");
        Fields fields = modal.content(StandardPage.class).regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields();
        StandardField field = fields.field("Имя");
        InputText inputText = field.control(InputText.class);
        inputText.val("");
        field.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        inputText.val("unique");
        field.shouldHaveValidationMessage(Condition.empty);
        button.click();

        TabsRegion tabs = modal.content(StandardPage.class).regions().region(1, TabsRegion.class);
        tabs.tab(1).shouldBeActive();

        // check tabs switch
        tabs.tab(0).click();
        tabs.tab(0).shouldBeActive();
        tabs.tab(1).click();

        fields = tabs.tab(1).content().widget(FormWidget.class).fields();
        field = fields.field("Название организации");
        field.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        inputText = field.control(InputText.class);
        inputText.val("test");
        field.shouldHaveValidationMessage(Condition.text("Организация test уже существует"));
        tabs.tab(0).click();
        button.click();

        tabs.tab(1).shouldBeActive();
        field.shouldHaveValidationMessage(Condition.text("Организация test уже существует"));
        inputText.val("unique");
        field.shouldHaveValidationMessage(Condition.empty);
        button.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
    }

    @Test
    public void testValidationTabMessageInPage() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().titleShouldHaveText("Подсветка невалидных полей в неактивных вкладках");

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        StandardButton button = form.toolbar().bottomLeft().button("Новая страница");
        button.shouldBeEnabled();
        button.click();

        StandardPage newPage = N2oSelenide.page(StandardPage.class);
        newPage.breadcrumb().titleShouldHaveText("Создание записи");
        button = newPage.toolbar().bottomRight().button("Сохранить");
        Fields fields = newPage.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields();
        StandardField field = fields.field("Имя");
        InputText inputText = field.control(InputText.class);
        inputText.val("");
        field.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        inputText.val("unique");
        field.shouldHaveValidationMessage(Condition.empty);
        button.click();

        TabsRegion tabs = newPage.regions().region(1, TabsRegion.class);
        tabs.tab(1).shouldBeActive();

        // check tabs switch
        tabs.tab(0).click();
        tabs.tab(0).shouldBeActive();
        tabs.tab(1).click();

        fields = tabs.tab(1).content().widget(FormWidget.class).fields();
        field = fields.field("Название организации");
        field.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        inputText = field.control(InputText.class);
        inputText.val("test");
        field.shouldHaveValidationMessage(Condition.text("Организация test уже существует"));
        tabs.tab(0).click();
        button.click();

        tabs.tab(1).shouldBeActive();
        field.shouldHaveValidationMessage(Condition.text("Организация test уже существует"));
        inputText.val("unique");
        field.shouldHaveValidationMessage(Condition.empty);
        button.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
    }
}
