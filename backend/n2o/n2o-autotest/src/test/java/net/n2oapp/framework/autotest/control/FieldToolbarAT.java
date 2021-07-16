package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersIOPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldToolbarAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/field_toolbar/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/field_toolbar/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/field_toolbar/modal.page.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oObjectsPack(), new N2oDataProvidersIOPack());
    }

    @Test
    public void testFieldToolbar() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Fields fields = page.widget(FormWidget.class).fields();
        Toolbar toolbar = fields.field("id").toolbar();

        toolbar.button("Ссылка").shouldBeDisabled();
        toolbar.button("Показать текст").shouldBeDisabled();
        toolbar.button("Открыть модальное окно").shouldBeDisabled();
        toolbar.button("Открыть страницу").shouldBeDisabled();

        fields.field("id").control(InputText.class).val("test");

        toolbar.button("Ссылка").shouldBeEnabled();
        toolbar.button("Показать текст").shouldBeEnabled();
        toolbar.button("Открыть модальное окно").shouldBeEnabled();
        toolbar.button("Открыть страницу").shouldBeEnabled();

        toolbar.button("Ссылка").click();
        Selenide.switchTo().window(1);
        page.urlShouldMatches("https://yandex.ru.*");
        Selenide.switchTo().window(0);

        fields.field("id").control(InputText.class).val("test");
        toolbar.button("Показать текст").click();
        page.alerts().alert(0).shouldHaveColor(Colors.SUCCESS);
        page.alerts().alert(0).shouldHaveText("test");

        toolbar.button("Открыть модальное окно").click();
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Другая страница");
        modal.content(SimplePage.class).widget(FormWidget.class).fields().field("С другой страницы").shouldExists();
        modal.close();

        toolbar.button("Открыть страницу").click();

        page.breadcrumb().titleShouldHaveText("Другая страница");
        page.widget(FormWidget.class).fields().field("С другой страницы").shouldExists();
    }

    @Test
    public void testFieldToolbarSubmenu() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Fields fields = page.widget(FormWidget.class).fields();

        DropdownButton dropdownButton = fields.field("sub").toolbar().dropdown();
        dropdownButton.shouldHaveItems(4);
        dropdownButton.click();
        dropdownButton.menuItem("Ссылка").shouldBeDisabled();
        dropdownButton.menuItem("Показать текст").shouldBeDisabled();
        dropdownButton.menuItem("Открыть модальное окно").shouldBeDisabled();
        dropdownButton.menuItem("Открыть страницу").shouldBeDisabled();
        fields.field("sub").control(InputText.class).val("test");
        dropdownButton.click();
        dropdownButton.menuItem("Ссылка").shouldBeEnabled();
        dropdownButton.menuItem("Показать текст").shouldBeEnabled();
        dropdownButton.menuItem("Открыть модальное окно").shouldBeEnabled();
        dropdownButton.menuItem("Открыть страницу").shouldBeEnabled();

        dropdownButton.menuItem("Ссылка").click();
        Selenide.switchTo().window(1);
        page.urlShouldMatches("https://yandex.ru.*");
        Selenide.switchTo().window(0);

        fields.field("id").control(InputText.class).val("test");
        fields.field("sub").control(InputText.class).val("notForShow");
        dropdownButton.click();
        dropdownButton.menuItem("Показать текст").click();
        page.alerts().alert(0).shouldHaveColor(Colors.SUCCESS);
        page.alerts().alert(0).shouldHaveText("test");

        dropdownButton.click();
        dropdownButton.menuItem("Открыть модальное окно").click();
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Другая страница");
        modal.content(SimplePage.class).widget(FormWidget.class).fields().field("С другой страницы").shouldExists();
        modal.close();

        dropdownButton.click();
        dropdownButton.menuItem("Открыть страницу").click();
        page.breadcrumb().titleShouldHaveText("Другая страница");
        page.widget(FormWidget.class).fields().field("С другой страницы").shouldExists();
    }
}
