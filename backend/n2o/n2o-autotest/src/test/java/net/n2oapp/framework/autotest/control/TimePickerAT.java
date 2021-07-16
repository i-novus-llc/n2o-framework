package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.TimePicker;
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
 * Автотест компонента ввода времени
 */
public class TimePickerAT extends AutoTestBase {

    private Fields fields;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/time_picker/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        fields = page.widget(FormWidget.class).fields();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testTimePicker() {
        TimePicker time = fields.field("Time1").control(TimePicker.class);
        time.shouldExists();
        time.shouldNotHavePrefix();

        time.shouldBeCollapsed();
        time.expand();
        time.shouldBeExpanded();
        time.collapse();
        time.shouldBeCollapsed();

        time.shouldHaveValue("15 ч 25 мин 30 сек");
        time.shouldSelectedHoursMinutesSeconds("15", "25", "30");

        time.selectHoursMinutesSeconds("19", "10", "15");
        time.shouldHaveValue("19 ч 10 мин 15 сек");
        time.shouldSelectedHoursMinutesSeconds("19", "10", "15");

        // time-format=hh:mm, mode=hours,minutes
        TimePicker time2 = fields.field("Time2").control(TimePicker.class);
        time2.shouldExists();
        time2.shouldHavePrefix("From: ");
        time2.shouldHaveValue("10:18");
        time2.shouldSelectedHoursMinutes("10", "18");

        // time-format=hh
        TimePicker time3 = fields.field("Time3").control(TimePicker.class);
        time3.shouldExists();
        time3.shouldBeEmpty();
        time3.selectHoursMinutesSeconds("05", "10", "15");
        // ignore minutes, seconds
        time3.shouldHaveValue("05");

        // time-format=mm, mode=minutes
        TimePicker time4 = fields.field("Time4").control(TimePicker.class);
        time4.shouldExists();
        time4.shouldBeEmpty();
        time4.selectMinutes("09");
        time4.shouldHaveValue("09 мин");
    }
}
