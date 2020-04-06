package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.impl.component.control.N2oDateInput;
import net.n2oapp.framework.autotest.impl.component.control.N2oInputText;
import net.n2oapp.framework.autotest.impl.component.field.N2oIntervalField;
import net.n2oapp.framework.autotest.impl.component.widget.N2oFormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест компонента интервала
 */
public class IntervalFieldAT extends AutoTestBase {

    @BeforeAll
    public static void BeforeAll() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/interval_field/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testInput() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        N2oIntervalField interval = page.single().widget(N2oFormWidget.class).fields().field("Интервал", N2oIntervalField.class);
        N2oInputText inputBegin = interval.begin(N2oInputText.class);
        N2oInputText inputEnd = interval.end(N2oInputText.class);
        inputBegin.shouldHaveValue("5");
        inputEnd.shouldHaveValue("10");
        inputEnd.clickPlusStepButton();
    }

    @Test
    public void testIntervalWithDate() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        N2oIntervalField interval = page.single().widget(N2oFormWidget.class).fields().field("Дата", N2oIntervalField.class);
        N2oDateInput inputBegin = interval.begin(N2oDateInput.class);
        N2oDateInput inputEnd = interval.end(N2oDateInput.class);
        inputBegin.shouldHaveValue("21.11.1999");
        inputEnd.shouldHaveValue("");
    }
}
