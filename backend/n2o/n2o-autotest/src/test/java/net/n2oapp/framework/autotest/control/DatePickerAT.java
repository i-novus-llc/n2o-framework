package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oDateInput;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест компонента ввода даты
 */
public class DatePickerAT extends AutoTestBase {

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
    public void testDatePicker() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/date_picker/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        N2oSimplePage page = open(N2oSimplePage.class);
        page.shouldExists();

        N2oDateInput date = page.single().widget(FormWidget.class).fields().field("Date1")
                .control(N2oDateInput.class);
        date.shouldExists();

        date.val("20.02.2020");
        date.shouldHaveValue("20.02.2020");
        // проверка, что значения, выходящие за границы min/max, не вводятся
        date.val("15.01.2020");
        date.shouldHaveValue("");
        date.val("15.03.2020");
        date.shouldHaveValue("");


        date = page.single().widget(FormWidget.class).fields().field("Date2")
                .control(N2oDateInput.class);
        date.shouldExists();

        date.shouldHaveValue("01/01/2020 00:00:00");
    }
}
