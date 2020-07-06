package net.n2oapp.framework.autotest.widget.calendar;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarToolbar;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarWidget;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.CalendarViewType;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для виджета Календарь
 */
public class CalendarAT extends AutoTestBase {
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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testCalendar() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/createEvent.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/selectEvent.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        CalendarWidget calendar = page.single().widget(CalendarWidget.class);
        calendar.shouldExists();
        CalendarToolbar toolbar = calendar.toolbar();
        toolbar.shouldHaveActiveView(CalendarViewType.MONTH);


    }
}
