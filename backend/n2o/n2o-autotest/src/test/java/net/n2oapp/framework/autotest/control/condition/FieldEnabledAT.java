package net.n2oapp.framework.autotest.control.condition;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/condition/enabled/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testEnabled() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup type = fields.field("type").control(RadioGroup.class);
        InputText field = fields.field("message").control(InputText.class);

        type.shouldBeEmpty();
        field.shouldBeDisabled();

        type.check("enabled");
        field.shouldBeEnabled();
        field.val("test");
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
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/enabled/static/index.page.xml"));
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
}
