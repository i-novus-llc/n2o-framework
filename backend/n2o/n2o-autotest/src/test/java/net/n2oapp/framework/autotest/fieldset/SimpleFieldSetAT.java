package net.n2oapp.framework.autotest.fieldset;

import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для простого филдсета
 */
public class SimpleFieldSetAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testFieldSet() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();

        fieldsets.shouldHaveSize(2);
        SimpleFieldSet fieldset = fieldsets.fieldset(SimpleFieldSet.class);
        fieldset.shouldNotHaveLabel();
        Fields fields = fieldset.fields();
        fields.shouldHaveSize(1);
        fields.field("field1").shouldHaveLabelLocation(FieldSet.LabelPosition.TOP_LEFT);
        InputText field1 = fields.field("field1").control(InputText.class);

        fieldset = fieldsets.fieldset(1, SimpleFieldSet.class);
        fieldset.shouldHaveLabel("Заголовок test");
        fieldset.shouldHaveDescription("Подзаголовок филдсета");
        field1.val("123");
        fieldset.shouldHaveLabel("Заголовок 123");

        fields = fieldset.fields();
        fields.shouldHaveSize(2);
        StandardField field2 = fields.field("field2");
        field2.shouldHaveLabelLocation(FieldSet.LabelPosition.LEFT);
        field2.control(InputText.class).shouldBeDisabled();
    }

    @Test
    public void testVisible() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/simple/visible/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        fieldsets.shouldHaveSize(3);

        InputText inputText = fieldsets.fieldset(0, SimpleFieldSet.class).fields().field("test").control(InputText.class);
        inputText.shouldExists();

        SimpleFieldSet set1 = fieldsets.fieldset(1, SimpleFieldSet.class);
        SimpleFieldSet set2 = fieldsets.fieldset(2, SimpleFieldSet.class);
        set1.shouldBeHidden();
        set2.shouldBeHidden();

        inputText.val("test");
        set1.shouldBeHidden();
        set2.shouldBeVisible();
        set2.fields().field("field2").shouldExists();
    }

    @Test
    public void testEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/simple/enabled/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        fieldsets.shouldHaveSize(3);

        InputText inputText = fieldsets.fieldset(0, SimpleFieldSet.class).fields().field("test").control(InputText.class);
        inputText.shouldExists();

        InputText set1Field = fieldsets.fieldset(1, SimpleFieldSet.class)
                .fields().field("field1").control(InputText.class);
        InputText set2Field = fieldsets.fieldset(2, SimpleFieldSet.class)
                .fields().field("field2").control(InputText.class);
        set1Field.shouldBeDisabled();
        set1Field.shouldBeDisabled();

        inputText.val("test");
        set1Field.shouldBeDisabled();
        set2Field.shouldBeEnabled();
    }
}