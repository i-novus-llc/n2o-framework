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
 * Автотест для условия видимости полей
 */
public class FieldVisibleAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/condition/visible/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testVisible() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup type = fields.field("type").control(RadioGroup.class);
        InputText field = fields.field("message").control(InputText.class);

        type.shouldBeEmpty();
        field.shouldNotExists();

        type.check("visible");
        field.shouldExists();
        field.val("test");
        field.shouldHaveValue("test");

        type.check("not visible");
        field.shouldNotExists();

        type.check("visible");
        field.shouldHaveValue("test");
    }

    @Test
    public void testFormFieldsVisible() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/condition/visible/form_fields/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        InputText field = fields.field("hidden").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("without visible").control(InputText.class);
        field.shouldExists();

        field = fields.field("visible=true").control(InputText.class);
        field.shouldExists();

        field = fields.field("visible=false").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=true").control(InputText.class);
        field.shouldExists();

        field = fields.field("dependencies[apply-on-init].visibility=false").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=undefined").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=null").control(InputText.class);
        field.shouldNotExists();
    }

    @Test
    public void testDynamicVisible() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/condition/visible/dynamic/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        RadioGroup fieldsetVisible = fields.field("fieldset_visible").control(RadioGroup.class);
        InputText field = fields.field("hidden_field").control(InputText.class);

        fieldsetVisible.shouldHaveValue("visible");
        field.shouldNotExists();

        field = fields.field("without visible").control(InputText.class);
        field.shouldExists();

        field = fields.field("visible=true").control(InputText.class);
        field.shouldExists();

        field = fields.field("visible=false").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=true").control(InputText.class);
        field.shouldExists();

        field = fields.field("dependencies[apply-on-init].visibility=false").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=undefined").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=null").control(InputText.class);
        field.shouldNotExists();

        fieldsetVisible.check("hidden");

        field = fields.field("without visible").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("visible=true").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("visible=false").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=true").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=false").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=undefined").control(InputText.class);
        field.shouldNotExists();

        field = fields.field("dependencies[apply-on-init].visibility=null").control(InputText.class);
        field.shouldNotExists();
    }
}
