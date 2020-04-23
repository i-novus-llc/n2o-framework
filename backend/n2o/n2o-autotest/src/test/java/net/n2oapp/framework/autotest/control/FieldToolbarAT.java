package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.collection.N2oToolbar;
import net.n2oapp.framework.autotest.impl.component.button.N2oDropdownButton;
import net.n2oapp.framework.autotest.impl.component.control.N2oInputText;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.impl.component.widget.N2oFormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersIOPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

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
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/field_toolbar/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/field_toolbar/modal.page.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack(), new N2oObjectsPack(), new N2oDataProvidersIOPack());
    }

    @Test
    public void testFieldToolbar() {
        SimplePage page = open(N2oSimplePage.class);
        page.shouldExists();
        Fields fields = page.single().widget(N2oFormWidget.class).fields();
        Toolbar toolbar = new N2oToolbar();
        toolbar.setElements(fields.field("id").element().$$(".btn.btn-secondary"));

        toolbar.button("Ссылка").shouldBeDisabled();
        toolbar.button("Показать текст").shouldBeDisabled();
        toolbar.button("Открыть модальное окно").shouldBeDisabled();
        toolbar.button("Открыть страницу").shouldBeDisabled();

        fields.field("id").control(N2oInputText.class).val("test");

        toolbar.button("Ссылка").shouldBeEnabled();
        toolbar.button("Показать текст").shouldBeEnabled();
        toolbar.button("Открыть модальное окно").shouldBeEnabled();
        toolbar.button("Открыть страницу").shouldBeEnabled();

        toolbar.button("Ссылка").click();
        getWebDriver().getCurrentUrl().contains("text=test");
        Selenide.back();

        fields.field("id").control(N2oInputText.class).val("test");
        toolbar.button("Показать текст").click();
        "test".equals(page.element().$("div.n2o-alerts div.n2o-alert-body-text").val());

        toolbar.button("Открыть модальное окно").click();
        Modal modal = N2oSelenide.modal();
        modal.setElement(page.element().$("div.modal-content"));
        modal.shouldHaveTitle("Другая страница");
        modal.content(SimplePage.class).single().widget(FormWidget.class).fields().field("С другой страницы").shouldExists();
        modal.close();

        toolbar.button("Открыть страницу").click();
        SelenideElement openPage = page.element().$(".application-body.container-fluid");
        "Другая страница".equals(openPage.$(".active.breadcrumb-item").text());
        "С другой страницы".equals(openPage.$("div.n2o-fieldset label").text());
    }

    @Test
    public void testFieldToolbarSubmenu() {
        SimplePage page = open(N2oSimplePage.class);
        page.shouldExists();
        Fields fields = page.single().widget(N2oFormWidget.class).fields();

        DropdownButton dropdownButton = new N2oDropdownButton();
        dropdownButton.setElement(fields.field("sub").element().$(".n2o-dropdown-control.dropdown-toggle"));
        dropdownButton.click();
        dropdownButton.menuItem("Ссылка").shouldBeDisabled();
        dropdownButton.menuItem("Показать текст").shouldBeDisabled();
        dropdownButton.menuItem("Открыть модальное окно").shouldBeDisabled();
        dropdownButton.menuItem("Открыть страницу").shouldBeDisabled();
        fields.field("id").control(N2oInputText.class).val("test");
        dropdownButton.click();
        dropdownButton.menuItem("Ссылка").shouldBeEnabled();
        dropdownButton.menuItem("Показать текст").shouldBeEnabled();
        dropdownButton.menuItem("Открыть модальное окно").shouldBeEnabled();
        dropdownButton.menuItem("Открыть страницу").shouldBeEnabled();

        dropdownButton.menuItem("Ссылка").click();
        getWebDriver().getCurrentUrl().contains("text=test");
        Selenide.back();

        fields.field("id").control(N2oInputText.class).val("test");
        fields.field("sub").control(N2oInputText.class).val("notForShow");
        dropdownButton.click();
        dropdownButton.menuItem("Показать текст").click();
        "test".equals(page.element().$("div.n2o-alerts div.n2o-alert-body-text").val());

        dropdownButton.menuItem("Открыть модальное окно").click();
        Modal modal = N2oSelenide.modal();
        modal.setElement(page.element().$("div.modal-content"));
        modal.shouldHaveTitle("Другая страница");
        modal.content(SimplePage.class).single().widget(FormWidget.class).fields().field("С другой страницы").shouldExists();
        modal.close();

        dropdownButton.click();
        dropdownButton.menuItem("Открыть страницу").click();
        SelenideElement openPage = page.element().$(".application-body.container-fluid");
        "Другая страница".equals(openPage.$(".active.breadcrumb-item").text());
        "С другой страницы".equals(openPage.$("div.n2o-fieldset label").text());
    }
}
