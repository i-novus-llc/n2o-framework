package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;
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
 * Автотест компонента группы чекбоксов
 */
public class CheckboxGroupAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testCheckboxGroup() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/checkbox_group/simple/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        CheckboxGroup checkboxGroup = page.widget(FormWidget.class).fields().field("Checkboxes1")
                .control(CheckboxGroup.class);
        checkboxGroup.shouldExists();

        checkboxGroup.shouldHaveOptions("One", "Two", "Three");
        checkboxGroup.shouldBeEmpty();
        checkboxGroup.check("One");
        checkboxGroup.check("Two");
        checkboxGroup.check("Three");
        checkboxGroup.shouldBeChecked("One");
        checkboxGroup.shouldBeChecked("Two");
        checkboxGroup.shouldBeChecked("Three");

        checkboxGroup.uncheck("Two");
        checkboxGroup.shouldBeUnchecked("Two");

        checkboxGroup.shouldHaveTooltip("One");
        checkboxGroup.shouldHaveTooltip("Two");
        checkboxGroup.shouldHaveTooltip("Three");
    }

    @Test
    public void defaultValue() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/checkbox_group/default_value/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        CheckboxGroup checkboxGroup = page.widget(FormWidget.class).fields().field("Группа чекбоксов").control(CheckboxGroup.class);
        checkboxGroup.shouldBeUnchecked("Один");
        checkboxGroup.shouldBeChecked("Два");
        checkboxGroup.shouldBeUnchecked("Три");
    }
}
