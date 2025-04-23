package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
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
 * Автотест для проверки резолва атрибутов для полей
 */
class FieldAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
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
    void testResolveAttributes() {

        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/field/resolve_attributes/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        Checkbox checkbox = fields.field("checkbox").control(Checkbox.class);
        checkbox.shouldExists();

        checkbox.shouldNotBeChecked();
        StandardField field1 = fields.field("Unchecked_label");
        field1.shouldExists();
        field1.control(InputText.class).shouldHavePlaceholder("Unchecked_placeholder");
        field1.shouldHaveDescription("Unchecked_description");
        field1.clickHelp();
        page.popover("Unchecked_help").shouldBeVisible();

        fields.field(2).shouldHaveLabel("Label2");
        fields.field(3).shouldHaveLabel("Label3");

        checkbox.setChecked(true);
        checkbox.shouldBeChecked();
        field1 = fields.field("Checked_label");
        field1.shouldExists();
        field1.control(InputText.class).shouldHavePlaceholder("Checked_placeholder");
        field1.shouldHaveDescription("Checked_description");
        field1.clickHelp();
        page.popover("Checked_help").shouldBeVisible();

        fields.field(2).shouldHaveEmptyLabel();
        fields.field(3).shouldNotHaveLabel();
    }
}