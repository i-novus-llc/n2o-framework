package net.n2oapp.framework.autotest.fieldset;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.fieldset.LineFieldSet;
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
 * Автотест для филдсета с горизонтальным делителем
 */
public class LineFieldSetAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testLineFieldSet() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/list/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();

        // empty fieldset with empty label
        LineFieldSet fieldset = fieldsets.fieldset(LineFieldSet.class);
        fieldset.shouldBeEmpty();
        fieldset.shouldNotHaveLabel();

        // expanded
        fieldset = fieldsets.fieldset(1, LineFieldSet.class);
        fieldset.fields().shouldHaveSize(3);
        fieldset.shouldBeCollapsible();
        fieldset.shouldBeExpanded();
        fieldset.shouldHaveLabel("Line2");
        fieldset.collapseContent();
        fieldset.shouldBeCollapsed();
        fieldset.expandContent();
        fieldset.shouldBeExpanded();

        // collapsed
        fieldset = fieldsets.fieldset(2, LineFieldSet.class);
        fieldset.shouldBeCollapsed();
        fieldset.shouldBeCollapsible();
        fieldset.shouldHaveLabel("Line3 test");

        // not collapsible fieldset
        fieldset = fieldsets.fieldset(3, LineFieldSet.class);
        fieldset.shouldNotBeCollapsible();
        fieldset.shouldHaveLabel("Line4");

        fieldset = fieldsets.fieldset(4, LineFieldSet.class);
        fieldset.shouldNotHaveLabel();
    }

    @Test
    public void testVisible() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/list/visible/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        fieldsets.shouldHaveSize(3);

        InputText inputText = fieldsets.fieldset(0, SimpleFieldSet.class).fields().field("test").control(InputText.class);
        inputText.shouldExists();

        LineFieldSet line1 = fieldsets.fieldset(1, LineFieldSet.class);
        LineFieldSet line2 = fieldsets.fieldset(2, LineFieldSet.class);
        line1.shouldNotBeVisible();
        line2.shouldNotBeVisible();

        inputText.val("test");
        line1.shouldNotBeVisible();
        line2.shouldBeVisible();
        line2.fields().field("field2").shouldExists();
    }

    @Test
    public void testEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/fieldset/list/enabled/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();
        fieldsets.shouldHaveSize(3);

        InputText inputText = fieldsets.fieldset(0, SimpleFieldSet.class).fields().field("test").control(InputText.class);
        inputText.shouldExists();

        InputText line1Field = fieldsets.fieldset(1, LineFieldSet.class)
                .fields().field("field1").control(InputText.class);
        InputText line2Field = fieldsets.fieldset(2, LineFieldSet.class)
                .fields().field("field2").control(InputText.class);
        line1Field.shouldBeDisabled();
        line1Field.shouldBeDisabled();

        inputText.val("test");
        line1Field.shouldBeDisabled();
        line2Field.shouldBeEnabled();
    }
}
