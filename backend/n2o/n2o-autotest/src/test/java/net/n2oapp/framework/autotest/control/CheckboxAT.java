package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.Checkbox;
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
public class CheckboxAT extends AutoTestBase {

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
    public void testCheckbox() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/checkbox/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

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
}
