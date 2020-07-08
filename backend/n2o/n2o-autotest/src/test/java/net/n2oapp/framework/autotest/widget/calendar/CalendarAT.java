package net.n2oapp.framework.autotest.widget.calendar;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.calendar.*;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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

import java.time.LocalDate;

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

        // вид отображения МЕСЯЦ
        CalendarMonthView monthView = calendar.monthView();
        toolbar.shouldHaveActiveView(CalendarViewType.MONTH);
        // проверка лэйблов тулбара
        toolbar.shouldHaveLabel("июль 2020");
        toolbar.prevButton().click();
        toolbar.shouldHaveLabel("июнь 2020");
        toolbar.nextButton().click();
        toolbar.nextButton().click();
        toolbar.shouldHaveLabel("август 2020");
        toolbar.prevButton().click();
        // проверка выходных дней
        monthView.shouldBeDayOff("11");
        monthView.shouldBeDayOff("19");
        // наличие 2-x событий
        CalendarEvent event1 = monthView.event("Событие1");
        event1.shouldExists();
        CalendarEvent event2 = monthView.event("Событие2");
        event2.shouldExists();
        // проверка тултипов
        event1.shouldHaveTitle("Тултип для События1");
        event2.shouldNotHaveTitle();
        // клик по ячейке с событием
        // должна открываться форма на создание, а не на изменение
        monthView.clickOnCell("07");
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        modal.close();
        // клик по числу в ячейке должен открывать выбранный день
        monthView.clickOnDay("06");

        // вид отображения ДЕНЬ
        toolbar.shouldHaveActiveView(CalendarViewType.DAY);


        // вид отображения Повестка дня
        toolbar.agendaViewButton().click();
        toolbar.shouldHaveActiveView(CalendarViewType.AGENDA);
        TableWidget agendaTable = calendar.agendaView().table();
        TableHeaders headers = agendaTable.columns().headers();
        headers.shouldHaveSize(3);
        headers.header(0).shouldHaveTitle("Дата");
        headers.header(1).shouldHaveTitle("Дата");
        headers.header(2).shouldHaveTitle("Дата");


        // проверка сегодняшнего дня
        toolbar.monthViewButton().click();
        toolbar.todayButton().click();
        int today = LocalDate.now().getDayOfMonth();
        monthView.shouldBeToday(today > 9 ? "" + today : "0" + today);
    }
}
