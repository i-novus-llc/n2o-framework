package net.n2oapp.framework.autotest.condition.field.enabled;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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

class FieldEnabledAT extends AutoTestBase {

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }
    
    @Test
    void dynamicEnabled() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/enabled/dynamic/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup fieldEnabled = fields.field("field_enabled").control(RadioGroup.class);
        InputText field = fields.field("InputText").control(InputText.class);

        fieldEnabled.shouldHaveValue("enabled");
        field.shouldBeEnabled();

        fieldEnabled.check("disabled");

        field.shouldBeDisabled();
    }

    @Test
    void enabledByDependencyOnModal() {
        setResourcePath("net/n2oapp/framework/autotest/condition/field/enabled/by_dependency_on_modal");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/enabled/by_dependency_on_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/enabled/by_dependency_on_modal/subPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/enabled/by_dependency_on_modal/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        //проверка через модальное окно
        checkShowModal(page);
        //проверка через open-page
        checkOpenPage(page);
    }

    private static void checkOpenPage(StandardPage page) {
        StandardButton openPageBtn = page.toolbar().topLeft().button("Добавить через page");
        openPageBtn.shouldBeEnabled();

        openPageBtn.click();

        StandardPage subPage = N2oSelenide.page(StandardPage.class);
        subPage.shouldExists();

        subPage = N2oSelenide.page(StandardPage.class);
        subPage.shouldExists();

        FormWidget mainForm = subPage.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        FormWidget subForm = subPage.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class);
        subForm.shouldExists();
        mainForm.shouldExists();

        MaskedInput maskedInput = mainForm.fields().field("СНИЛС").control(MaskedInput.class);
        ButtonField buttonField = mainForm.fields().field("Поиск по СНИЛС", ButtonField.class);
        StandardField inputField = subForm.fields().field("Фамилия");
        InputText inputText = inputField.control(InputText.class);

        buttonField.shouldBeDisabled();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();

        maskedInput.setValue("111-111-111 11");
        buttonField.shouldBeEnabled();
        buttonField.click();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();

        maskedInput.setValue("555-750-462 12");
        buttonField.click();
        inputText.shouldBeDisabled();
        inputField.shouldNotBeRequired();

        subPage.breadcrumb().crumb(0).click();
        openPageBtn.click();

        subPage.shouldExists();

        buttonField.shouldBeDisabled();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();
    }

    private static void checkShowModal(StandardPage page) {
        StandardButton openModalBtn = page.toolbar().topLeft().button("Добавить через modal");
        openModalBtn.shouldBeEnabled();

        openModalBtn.click();

        StandardPage modal = N2oSelenide.modal().content(StandardPage.class);
        modal.shouldExists();

        FormWidget mainForm = modal.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        FormWidget subForm = modal.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class);
        subForm.shouldExists();
        mainForm.shouldExists();

        MaskedInput maskedInput = mainForm.fields().field("СНИЛС").control(MaskedInput.class);
        ButtonField buttonField = mainForm.fields().field("Поиск по СНИЛС", ButtonField.class);
        StandardField inputField = subForm.fields().field("Фамилия");
        InputText inputText = inputField.control(InputText.class);

        buttonField.shouldBeDisabled();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();

        maskedInput.setValue("111-111-111 11");
        buttonField.shouldBeEnabled();
        buttonField.click();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();

        maskedInput.setValue("555-750-462 12");
        buttonField.click();
        inputText.shouldBeDisabled();
        inputField.shouldNotBeRequired();

        N2oSelenide.modal().close();
        modal.shouldNotExists();
        openModalBtn.click();

        modal.shouldExists();

        buttonField.shouldBeDisabled();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();

        N2oSelenide.modal().close();
        modal.shouldNotExists();
    }
}
