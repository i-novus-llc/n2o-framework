package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oCheckbox;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест поля чекбокса
 */
public class CheckboxAT extends AutoTestBase {

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
    public void testCheckbox() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/checkbox/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oCheckbox input = page.single().widget(FormWidget.class).fields().field("Checkbox1")
                .control(N2oCheckbox.class);
        input.shouldExists();

        input.shouldBeChecked();
        input.setChecked(false);
        input.shouldBeUnchecked();


        input = page.single().widget(FormWidget.class).fields().field("Checkbox2")
                .control(N2oCheckbox.class);
        input.shouldExists();

        input.shouldBeUnchecked();
        input.setChecked(true);
        input.shouldBeChecked();
    }
}
