package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.Pills;
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
 * Автотест для компонента Таблетки
 */
public class PillsAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/pills/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/pills/pills.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testPillsCheckboxes() {
        Pills pills = page.widget(FormWidget.class).fields().field("Pills1")
                .control(Pills.class);
        pills.shouldExists();

        pills.shouldBeEmpty();
        pills.check("label2");
        pills.shouldBeChecked("label2");
        pills.uncheck("label2");
        pills.check("label1");
        pills.check("label3");
        pills.shouldBeChecked("label1");
        pills.shouldBeUnchecked("label2");
        pills.shouldBeChecked("label3");
    }

    @Test
    public void testPillsRadio() {
        Pills pills = page.widget(FormWidget.class).fields().field("Pills2")
                .control(Pills.class);
        pills.shouldExists();

        pills.shouldBeEmpty();
        pills.check("label5");
        pills.shouldBeChecked("label5");
        pills.check("label6");
        pills.shouldBeChecked("label6");
        pills.shouldBeUnchecked("label5");
        pills.uncheck("label6");
        pills.shouldBeUnchecked("label6");
    }
}
