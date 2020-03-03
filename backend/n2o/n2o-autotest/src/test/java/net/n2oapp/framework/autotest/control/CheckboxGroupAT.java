package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oCheckboxGroup;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест группы чекбоксов (checkbox-group)
 */
public class CheckboxGroupAT extends AutoTestBase {

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testCheckboxGroup() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/checkbox_group/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oCheckboxGroup checkboxGroup = page.single().widget(FormWidget.class).fields().field("Checkboxes1")
                .control(N2oCheckboxGroup.class);
        checkboxGroup.shouldExists();

        checkboxGroup.shouldBeUnchecked("One");
        checkboxGroup.shouldBeUnchecked("Two");
        checkboxGroup.shouldBeUnchecked("Three");
        checkboxGroup.check("One");
        checkboxGroup.check("Two");
        checkboxGroup.check("Three");
        checkboxGroup.shouldBeChecked("Two");
        checkboxGroup.uncheck("Two");
        checkboxGroup.shouldBeChecked("One");
        checkboxGroup.shouldBeUnchecked("Two");
        checkboxGroup.shouldBeChecked("Three");
    }
}
