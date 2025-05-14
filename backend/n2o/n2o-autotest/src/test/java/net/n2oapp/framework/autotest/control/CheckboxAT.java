package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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
 * Автотест компонента чекбокса
 */
class CheckboxAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    void testCheckbox() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/checkbox/index.page.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Checkbox checkbox = page.widget(FormWidget.class).fields().field("Checkbox1")
                .control(Checkbox.class);
        checkbox.shouldExists();

        checkbox.shouldBeChecked();
        checkbox.setChecked(false);
        checkbox.shouldBeEmpty();


        checkbox = page.widget(FormWidget.class).fields().field("Checkbox2")
                .control(Checkbox.class);
        checkbox.shouldExists();

        checkbox.shouldBeEmpty();
        checkbox.setChecked(true);
        checkbox.shouldBeChecked();
    }

    @Test
    void testUnchecked() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/checkbox/unchecked/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Checkbox checkboxNull = page.widget(FormWidget.class).fields().field("Init null").control(Checkbox.class);
        InputText isNull = page.widget(FormWidget.class).fields().field("isNull").control(InputText.class);
        Checkbox checkboxFalse = page.widget(FormWidget.class).fields().field("Init false").control(Checkbox.class);
        InputText isFalse = page.widget(FormWidget.class).fields().field("isFalse").control(InputText.class);

        checkboxNull.shouldExists();
        isNull.shouldExists();
        checkboxFalse.shouldExists();
        isFalse.shouldExists();

        checkboxNull.shouldNotBeChecked();
        isNull.shouldBeEmpty();
        checkboxFalse.shouldNotBeChecked();
        isFalse.shouldHaveValue("false");

        checkboxNull.setChecked(true);
        checkboxFalse.setChecked(true);

        isNull.shouldHaveValue("true");
        isFalse.shouldHaveValue("true");

        checkboxNull.setChecked(false);
        checkboxFalse.setChecked(false);

        isNull.shouldBeEmpty();
        isFalse.shouldHaveValue("false");
    }
}
