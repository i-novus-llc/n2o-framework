package net.n2oapp.framework.config.metadata.compile.widget.calendar;

import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.Calendar;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarWidgetComponent;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Тестирование компиляции виджета Календарь
 */
class CalendarWidgetCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack(),
                new N2oActionsPack()
        );
    }

    @Test
    void testCalendar() throws ParseException {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testCalendarCompile.page.xml")
                .get(new PageContext("testCalendarCompile"));

        Calendar calendar = (Calendar) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(calendar.getSrc(), is("CalendarWidget"));
        assertThat(calendar.getComponent(), allOf(
                hasProperty("size", is(20)),
                hasProperty("height", is("300px")),
                hasProperty("defaultView", is("month")),
                hasProperty("markDaysOff", is(true)),
                hasProperty("selectable", is(true)),
                hasProperty("step", is(30)),
                hasProperty("timeSlots", is(2)),
                hasProperty("titleFieldId", is("title")),
                hasProperty("tooltipFieldId", is("tooltip")),
                hasProperty("startFieldId", is("start")),
                hasProperty("endFieldId", is("end")),
                hasProperty("cellColorFieldId", is("color")),
                hasProperty("disabledFieldId", is("disabled"))
        ));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        Calendar calendar2 = (Calendar) page.getRegions().get("single").get(0).getContent().get(1);
        CalendarWidgetComponent component = calendar2.getComponent();
        assertThat(component, allOf(
                hasProperty("size", is(50)),
                hasProperty("height", is("500px")),
                hasProperty("date", is(simpleDateFormat.parse("2020-04-29T00:00:00"))),
                hasProperty("defaultView", is("work_week")),
                hasProperty("views", is(new String[]{"month", "day", "agenda", "work_week"})),
                hasProperty("minTime", is("08:00:00")),
                hasProperty("maxTime", is("20:00:00")),
                hasProperty("markDaysOff", is(false)),
                hasProperty("selectable", is(false)),
                hasProperty("step", is(60)),
                hasProperty("timeSlots", is(1)),
                hasProperty("resourceFieldId", is("resourceId")),
                hasProperty("resources", arrayWithSize(2))
        ));
        assertThat(component.getResources()[0], allOf(
                hasProperty("id", is("1")),
                hasProperty("title", is("Training room"))
        ));
        assertThat(component.getResources()[1], allOf(
                hasProperty("id", is("2")),
                hasProperty("title", is("Meeting room"))
        ));
        assertThat(component.getFormats(), allOf(
                hasEntry("dayFormat", "dd"),
                hasEntry("weekdayFormat", "eee")
        ));
        assertThat(component.getOnSelectSlot(), allOf(
                instanceOf(LinkActionImpl.class),
                hasProperty("operationId", is("create"))
        ));
        assertThat(component.getOnSelectEvent(), allOf(
                instanceOf(ShowModal.class),
                hasProperty("operationId", is("update"))
        ));
    }
}
