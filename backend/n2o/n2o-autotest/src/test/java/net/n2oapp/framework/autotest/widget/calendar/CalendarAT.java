package net.n2oapp.framework.autotest.widget.calendar;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.calendar.*;
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

    private CalendarWidget calendar;
    private CalendarToolbar toolbar;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        calendar = page.single().widget(CalendarWidget.class);
        calendar.shouldExists();
        toolbar = calendar.toolbar();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/createEvent.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/calendar/selectEvent.page.xml"));
    }

    @Test
    public void testCalendarMonthView() {
        toolbar.monthViewButton().click();
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
        event1.shouldHaveTooltipTitle("Тултип для События1");
        event2.shouldNotHaveTitle();
        // клик по ячейке с событием не вызывает ни одной формы
        event2.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldNotExists();
        // аналогично с ячейкой не содержащей события
        monthView.clickOnCell("15");
        modal = N2oSelenide.modal();
        modal.shouldNotExists();

        // клик по числу в ячейке должен открывать выбранный день
        monthView.clickOnDay("06");
        toolbar.shouldHaveActiveView(CalendarViewType.DAY);
        toolbar.shouldHaveLabel("понедельник июль 06");

        // проверка сегодняшнего дня
        toolbar.monthViewButton().click();
        toolbar.todayButton().click();
        int today = LocalDate.now().getDayOfMonth();
        monthView.shouldBeToday(today > 9 ? "" + today : "0" + today);
    }

    @Test
    public void testCalendarDayView() {
        toolbar.dayViewButton().click();
        toolbar.shouldHaveActiveView(CalendarViewType.DAY);
        CalendarTimeView dayView = calendar.dayView();

        // проверка лэйблов тулбара
        toolbar.shouldHaveLabel("понедельник июль 06");
        toolbar.prevButton().click();
        toolbar.shouldHaveLabel("воскресенье июль 05");
        toolbar.nextButton().click();
        toolbar.nextButton().click();
        toolbar.shouldHaveLabel("вторник июль 07");
        toolbar.prevButton().click();

        // проверка хэдеров
        CalendarTimeViewHeader header1 = dayView.header(0);
        header1.shouldHaveTitle("Конференц зал");
        CalendarTimeViewHeader header2 = dayView.header(1);
        header2.shouldHaveTitle("Переговорка");

        // клик по ячейке allDay
        // не должна открываться форма на создание
        header1.clickAllDay();
        Modal modal = N2oSelenide.modal();
        modal.shouldNotExists();
        // проверка, что клик по ячейке создает событие с заполненным временем и ресурсом
        dayView.clickCell(1, "4:30");
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        Fields fields = modal.content(SimplePage.class).single().widget(FormWidget.class).fields();
        net.n2oapp.framework.autotest.api.component.control.InputText name = fields.field("Название события").control(InputText.class);
        name.shouldBeEmpty();
        DateInterval date = fields.field("Дата").control(DateInterval.class);
        date.beginShouldHaveValue("06.07.2020 04:30:00");
        date.endShouldHaveValue("06.07.2020 05:00:00");
        RadioGroup resource = fields.field("Ресурс").control(RadioGroup.class);
        resource.shouldBeChecked("Конференц зал");
        modal.close();

        // наличие событий
        CalendarEvent event1 = dayView.event("Событие1");
        event1.shouldExists();
        // клик по событию
        // проверка, что открывается форма на создание, а не изменение из-за disabled=true
        event1.click();
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        modal.close();

        toolbar.nextButton().click();
        CalendarEvent event2 = dayView.event("Событие2");
        event2.shouldExists();
        // клик по ячейке с событием
        // проверка, что открывается форма на изменение
        event2.click();
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Просмотр события");
        fields = modal.content(SimplePage.class).single().widget(FormWidget.class).fields();
        name = fields.field("Название события").control(InputText.class);
        name.shouldBeEmpty();
        date = fields.field("Дата").control(DateInterval.class);
        date.beginShouldHaveValue("07.07.2020 13:00:00");
        date.endShouldHaveValue("07.07.2020 15:00:00");
        resource = fields.field("Ресурс").control(RadioGroup.class);
        resource.shouldBeChecked("Переговорка");
        modal.close();

        event2.shouldNotHaveTitle();
    }

    @Test
    public void testCalendarAgendaView() {
        toolbar.agendaViewButton().click();
        toolbar.shouldHaveActiveView(CalendarViewType.AGENDA);
        CalendarAgendaView agendaView = calendar.agendaView();
        // проверка лэйблов тулбара
        toolbar.shouldHaveLabel("06 июля — 05 авг.");
        toolbar.prevButton().click();
        toolbar.shouldHaveLabel("06 июня — 06 июля");
        toolbar.nextButton().click();
        toolbar.nextButton().click();
        toolbar.shouldHaveLabel("05 авг. — 04 сент.");
        toolbar.prevButton().click();
        // данные событий за период
        agendaView.shouldHaveSize(2);
        agendaView.eventShouldHaveDate(0, "пн июль 06");
        agendaView.eventShouldHaveTime(0, "15:00 — 16:00");
        agendaView.eventShouldHaveName(0, "Событие1");
        agendaView.eventShouldHaveDate(1, "вт июль 07");
        agendaView.eventShouldHaveTime(1, "13:00 — 15:00");
        agendaView.eventShouldHaveName(1, "Событие2");
    }

    @Test
    public void testCalendarWeekView() {
        toolbar.weekViewButton().click();
        toolbar.shouldHaveActiveView(CalendarViewType.WEEK);
        CalendarTimeView weekView = calendar.weekView();

        // проверка лэйблов тулбара
        toolbar.shouldHaveLabel("понедельник июль 06");
        toolbar.prevButton().click();
        toolbar.shouldHaveLabel("воскресенье июль 05");
        toolbar.nextButton().click();
        toolbar.nextButton().click();
        toolbar.shouldHaveLabel("вторник июль 07");
        toolbar.prevButton().click();

        // проверка хэдеров
        CalendarTimeViewHeader header1 = weekView.header(0);
        header1.shouldHaveTitle("Конференц зал");
        CalendarTimeViewHeader header2 = weekView.header(1);
        header2.shouldHaveTitle("Переговорка");
    }
}
