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

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testFieldSet() {
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        fieldsets.shouldHaveSize(2);
        SimpleFieldSet fieldset = fieldsets.fieldset(SimpleFieldSet.class);
        fieldset.shouldNotHaveLabel();
        Fields fields = fieldset.fields();
        fields.shouldHaveSize(1);
        fields.field("field1").shouldHaveLabelLocation(FieldSet.LabelPosition.TOP_LEFT);

        fieldset = fieldsets.fieldset(1, SimpleFieldSet.class);
        fieldset.shouldHaveLabel("Заголовок");
        fields = fieldset.fields();
        fields.shouldHaveSize(2);
        StandardField field2 = fields.field("field2");
        field2.shouldHaveLabelLocation(FieldSet.LabelPosition.LEFT);
        field2.control(InputText.class).shouldBeDisabled();
    }
}