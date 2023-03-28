package net.n2oapp.framework.autotest.control.condition;

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
 * Автотест для условия доступности полей
 */
public class FieldEnabledAT extends AutoTestBase {

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

    @Test
    public void testEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup type = fields.field("type").control(RadioGroup.class);
        InputText field = fields.field("message").control(InputText.class);

        type.shouldBeEmpty();
        field.shouldBeDisabled();

        type.check("enabled");
        field.shouldBeEnabled();
        field.click();
        field.setValue("test");
        field.shouldHaveValue("test");

        type.check("disabled");
        field.shouldBeDisabled();
        field.shouldHaveValue("test");
    }

    @Test
    public void testFormFieldsEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/condition/enabled/form_fields/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        InputText field = fields.field("without enable").control(InputText.class);
        field.shouldBeEnabled();

        field = fields.field("enable=true").control(InputText.class);
        field.shouldBeEnabled();

        field = fields.field("enable=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=true").control(InputText.class);
        field.shouldBeEnabled();

        field = fields.field("dependencies[apply-on-init].enabling=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=undefined").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=null").control(InputText.class);
        field.shouldBeDisabled();
    }

    @Test
    public void testDynamicEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/condition/enabled/dynamic/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup fieldsetEnabled = fields.field("fieldset_enabled").control(RadioGroup.class);
        InputText field = fields.field("without enable").control(InputText.class);

        fieldsetEnabled.shouldHaveValue("enabled");
        field.shouldBeEnabled();

        field = fields.field("enable=true").control(InputText.class);
        field.shouldBeEnabled();

        field = fields.field("enable=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=true").control(InputText.class);
        field.shouldBeEnabled();

        field = fields.field("dependencies[apply-on-init].enabling=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=undefined").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=null").control(InputText.class);
        field.shouldBeDisabled();

        fieldsetEnabled.check("disabled");

        field = fields.field("without enable").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("enable=true").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("enable=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=true").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=undefined").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies[apply-on-init].enabling=null").control(InputText.class);
        field.shouldBeDisabled();
    }

    @Test
    public void testStaticEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/condition/enabled/static/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup fieldsetEnabled = fields.field("fields_enabled").control(RadioGroup.class);
        InputText field = fields.field("enable=true").control(InputText.class);

        fieldsetEnabled.shouldHaveValue("enabled");
        field.shouldBeDisabled();

        field = fields.field("enable=fields_enabled").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]=true").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]=undefined").control(InputText.class);
        field.shouldBeDisabled();

        fieldsetEnabled.check("disabled");

        field = fields.field("enable=true").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("enable=fields_enabled").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]=true").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]=false").control(InputText.class);
        field.shouldBeDisabled();

        field = fields.field("dependencies.enabling[on=fields_enabled]=undefined").control(InputText.class);
        field.shouldBeDisabled();
    }

    @Test
    public void enabledByDependencyOnModal() {
        setJsonPath("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_modal");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_modal/subPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_modal/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        table.shouldExists();

        StandardButton button = table.toolbar().topLeft().button("Добавить");
        button.shouldBeEnabled();

        button.click();

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
        button.click();

        modal.shouldExists();

        buttonField.shouldBeDisabled();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();
    }

    @Test
    public void enabledByDependencyOnPage() {
        setJsonPath("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_page");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_page/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_page/subPage.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/by_dependency_on_page/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        table.shouldExists();

        StandardButton button = table.toolbar().topLeft().button("Добавить");
        button.shouldBeEnabled();

        button.click();

        StandardPage subPage = N2oSelenide.page(StandardPage.class);
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
        button.click();

        subPage.shouldExists();

        buttonField.shouldBeDisabled();
        inputText.shouldBeEnabled();
        inputField.shouldBeRequired();
    }
}
