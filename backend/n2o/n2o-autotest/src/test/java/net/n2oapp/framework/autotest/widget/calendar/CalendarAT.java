package net.n2oapp.framework.autotest.widget.calendar;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarEvent;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarToolbar;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarWidget;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.*;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.view.CalendarViewType;
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
        calendar = page.widget(CalendarWidget.class);
        calendar.shouldExists();
        toolbar = calendar.calendarToolbar();
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
        // наличие 3-x событий
        CalendarEvent event1 = monthView.event("Событие1");
        event1.shouldExists();
        CalendarEvent event2 = monthView.event("Событие2");
        event2.shouldExists();
        CalendarEvent event3 = monthView.event("All day event");
        event3.shouldExists();
        // проверка тултипов
        event1.shouldHaveTooltipTitle("Тултип для События1");
        event2.shouldNotHaveTooltip();
        // клик по ячейке с событием вызывает форму просмотра события
        event2.click();
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Просмотр события");
        Fields fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldHaveValue("Событие2");
        DateInterval date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("07.07.2020 13:00:00");
        date.endShouldHaveValue("07.07.2020 15:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeChecked("Переговорка");
        modal.close();

        // проверка, что клик по ячейке открывает форму создания события с заполненным временем на весь день
        monthView.clickOnCell("15");
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldBeEmpty();
        date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("15.07.2020 00:00:00");
        date.endShouldHaveValue("16.07.2020 00:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeEmpty();
        modal.close();

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
        CalendarDayView dayView = calendar.dayView();

        // проверка лэйблов тулбара
        toolbar.shouldHaveLabel("понедельник июль 06");
        toolbar.prevButton().click();
        toolbar.shouldHaveLabel("воскресенье июль 05");
        toolbar.nextButton().click();
        toolbar.nextButton().click();
        toolbar.shouldHaveLabel("вторник июль 07");
        toolbar.prevButton().click();

        // проверка хэдеров
        CalendarDayViewHeader header1 = dayView.header(0, CalendarDayViewHeader.class);
        header1.shouldHaveTitle("Конференц зал");
        CalendarDayViewHeader header2 = dayView.header(1, CalendarDayViewHeader.class);
        header2.shouldHaveTitle("Переговорка");

        // клик по ячейке allDay
        // проверка, что клик по ячейке открывает форму создания события с заполненным временем на весь день
        header2.clickAllDay();
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        Fields fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldBeEmpty();
        DateInterval date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("06.07.2020 00:00:00");
        date.endShouldHaveValue("07.07.2020 00:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeEmpty();
        modal.close();

        // наличие событий
        CalendarEvent event1 = dayView.event("Событие1");
        event1.shouldExists();
        event1.shouldHaveTooltipTitle("Тултип для События1");
        // клик по событию
        // проверка, что не открывается форма на просмотр события из-за disabled=true
        event1.click();
        modal = N2oSelenide.modal();
        modal.shouldNotExists();

        toolbar.nextButton().click();
        CalendarEvent event2 = dayView.event("Событие2");
        event2.shouldExists();
        event2.shouldNotHaveTooltip();
        // клик по ячейке с событием
        // проверка, что открывается форма на просмотр события
        event2.click();
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Просмотр события");
        fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldHaveValue("Событие2");
        date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("07.07.2020 13:00:00");
        date.endShouldHaveValue("07.07.2020 15:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeChecked("Переговорка");
        modal.close();

        // проверка наличия события на весь день
        CalendarEvent allDayEvent = header1.allDayEvent("All day event");
        allDayEvent.shouldExists();
        // клик по ячейке с событием
        // проверка, что открывается форма на просмотр события на весь день
        allDayEvent.click();
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Просмотр события");
        fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldHaveValue("All day event");
        date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("07.07.2020 00:00:00");
        date.endShouldHaveValue("08.07.2020 00:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeChecked("Конференц зал");
        modal.close();

        // проверка, что клик по ячейке открывает форму создания события с заполненным временем и ресурсом
        dayView.clickCell(1, "4:30");
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldBeEmpty();
        date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("07.07.2020 04:30:00");
        date.endShouldHaveValue("07.07.2020 05:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeChecked("Переговорка");

        // СОЗДАНИЕ события
        fields.field("Название события").control(InputText.class).val("Новое событие");
        modal.toolbar().bottomRight().button("Сохранить").click();
        modal.shouldNotExists();
        // проверка, что событие появилось в календаре
        CalendarEvent event3 = dayView.event("Новое событие");
        event3.shouldExists();

        // УДАЛЕНИЕ события
        event3.click();
        modal.content(SimplePage.class).widget(FormWidget.class).toolbar()
                .topRight().button("Удалить").click();
        modal.shouldNotExists();
        event3.shouldNotExists();
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
        agendaView.shouldHaveSize(3);
        agendaView.eventShouldHaveDate(0, "пн июль 06");
        agendaView.eventShouldHaveTime(0, "15:00 — 16:00");
        agendaView.eventShouldHaveName(0, "Событие1");
        agendaView.eventShouldHaveDate(1, "вт июль 07");
        agendaView.eventShouldHaveTime(1, "0:00");
        agendaView.eventShouldHaveName(1, "All day event");
        agendaView.eventShouldHaveTime(2, "13:00 — 15:00");
        agendaView.eventShouldHaveName(2, "Событие2");
    }

    @Test
    public void testCalendarWeekView() {
        toolbar.weekViewButton().click();
        toolbar.shouldHaveActiveView(CalendarViewType.WEEK);
        CalendarWeekView weekView = calendar.weekView();

        // проверка лэйблов тулбара
        toolbar.shouldHaveLabel("06 июля — 12 июля");
        toolbar.prevButton().click();
        toolbar.shouldHaveLabel("29 июня — 05 июля");
        toolbar.nextButton().click();
        toolbar.nextButton().click();
        toolbar.shouldHaveLabel("13 июля — 19 июля");
        toolbar.prevButton().click();

        // проверка хэдеров
        CalendarWeekViewHeader header1 = weekView.header(0, CalendarWeekViewHeader.class);
        header1.shouldHaveTitle("Конференц зал");
        CalendarWeekViewHeader header2 = weekView.header(1, CalendarWeekViewHeader.class);
        header2.shouldHaveTitle("Переговорка");

        // клик по ячейке allDay
        // проверка, что клик по ячейке открывает форму создания события с заполненным временем на весь день
        header1.clickAllDay();
        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        Fields fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldBeEmpty();
        DateInterval date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("09.07.2020 00:00:00");
        date.endShouldHaveValue("10.07.2020 00:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeEmpty();
        modal.close();

        // проверка, что клик по ячейке открывает форму создания события с заполненным временем и ресурсом
        weekView.clickCell(0, "09", "3:00");
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Создание события");
        fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldBeEmpty();
        date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("09.07.2020 03:00:00");
        date.endShouldHaveValue("09.07.2020 03:30:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeChecked("Конференц зал");
        modal.close();

        // наличие событий
        CalendarEvent event1 = weekView.event("Событие1");
        event1.shouldExists();
        event1.shouldHaveTooltipTitle("Тултип для События1");
        // клик по событию
        // проверка, что не открывается форма на просмотр события из-за disabled=true
        event1.click();
        modal = N2oSelenide.modal();
        modal.shouldNotExists();

        CalendarEvent event2 = weekView.event("Событие2");
        event2.shouldExists();
        event2.shouldNotHaveTooltip();
        // клик по ячейке с событием
        // проверка, что открывается форма на просмотр события
        event2.click();
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Просмотр события");
        fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldHaveValue("Событие2");
        date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("07.07.2020 13:00:00");
        date.endShouldHaveValue("07.07.2020 15:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeChecked("Переговорка");
        modal.close();

        // проверка наличия события на весь день
        CalendarEvent allDayEvent = header1.allDayEvent("All day event");
        allDayEvent.shouldExists();
        // клик по ячейке с событием
        // проверка, что открывается форма на просмотр события на весь день
        allDayEvent.click();
        modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Просмотр события");
        fields = modal.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.field("Название события").control(InputText.class).shouldHaveValue("All day event");
        date = fields.field("Дата").control(DateInterval.class);
        date.shouldBeCollapsed();
        date.beginShouldHaveValue("07.07.2020 00:00:00");
        date.endShouldHaveValue("08.07.2020 00:00:00");
        fields.field("Ресурс").control(RadioGroup.class).shouldBeChecked("Конференц зал");
        modal.close();

        // клик по числу в хэдере должен открывать выбранный день
        header1.clickDayCell("09");
        toolbar.shouldHaveActiveView(CalendarViewType.DAY);
        toolbar.shouldHaveLabel("четверг июль 09");
    }
}
