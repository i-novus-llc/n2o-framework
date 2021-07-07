package net.n2oapp.framework.config.metadata.compile.widget.calendar;

import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.Calendar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


/**
 * Тестирование компиляции виджета Календарь
 */
public class CalendarWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
    }

    @Test
    public void testCalendar() throws ParseException {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testCalendarCompile.page.xml")
                .get(new PageContext("testCalendarCompile"));

        Calendar calendar = (Calendar) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(calendar.getSrc(), is("CalendarWidget"));
        assertThat(calendar.getName(), is("calendar1"));
        assertThat(calendar.getComponent().getSize(), is(20));
        assertThat(calendar.getComponent().getDefaultView(), is("month"));
        assertThat(calendar.getComponent().getMarkDaysOff(), is(true));
        assertThat(calendar.getComponent().getSelectable(), is(true));
        assertThat(calendar.getComponent().getStep(), is(30));
        assertThat(calendar.getComponent().getTimeSlots(), is(2));
        assertThat(calendar.getComponent().getTitleFieldId(), is("title"));
        assertThat(calendar.getComponent().getTooltipFieldId(), is("tooltip"));
        assertThat(calendar.getComponent().getStartFieldId(), is("start"));
        assertThat(calendar.getComponent().getEndFieldId(), is("end"));
        assertThat(calendar.getComponent().getCellColorFieldId(), is("color"));
        assertThat(calendar.getComponent().getDisabledFieldId(), is("disabled"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        Calendar calendar2 = (Calendar) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(calendar2.getName(), is("calendar2"));
        assertThat(calendar2.getComponent().getSize(), is(50));
        assertThat(calendar2.getComponent().getHeight(), is("500px"));
        assertThat(calendar2.getComponent().getDate(), is(simpleDateFormat.parse("2020-04-29T00:00:00")));
        assertThat(calendar2.getComponent().getDefaultView(), is("work_week"));
        assertThat(calendar2.getComponent().getViews(), is(new String[]{"month", "day", "agenda", "work_week"}));
        assertThat(calendar2.getComponent().getMinTime(), is("08:00:00"));
        assertThat(calendar2.getComponent().getMaxTime(), is("20:00:00"));
        assertThat(calendar2.getComponent().getMarkDaysOff(), is(false));
        assertThat(calendar2.getComponent().getSelectable(), is(false));
        assertThat(calendar2.getComponent().getStep(), is(60));
        assertThat(calendar2.getComponent().getTimeSlots(), is(1));
        assertThat(calendar2.getComponent().getResourceFieldId(), is("resourceId"));
        assertThat(calendar2.getComponent().getResources().length, is(2));
        assertThat(calendar2.getComponent().getResources()[0].getId(), is("1"));
        assertThat(calendar2.getComponent().getResources()[0].getTitle(), is("Training room"));
        assertThat(calendar2.getComponent().getResources()[1].getId(), is("2"));
        assertThat(calendar2.getComponent().getResources()[1].getTitle(), is("Meeting room"));
        assertThat(calendar2.getComponent().getFormats().get("dayFormat"), is("dd"));
        assertThat(calendar2.getComponent().getFormats().get("weekdayFormat"), is("eee"));
        assertThat(calendar2.getComponent().getOnSelectSlot(), instanceOf(LinkActionImpl.class));
        assertThat(((LinkActionImpl) calendar2.getComponent().getOnSelectSlot()).getOperationId(), is("create"));
        assertThat(calendar2.getComponent().getOnSelectEvent(), instanceOf(ShowModal.class));
        assertThat(((ShowModal) calendar2.getComponent().getOnSelectEvent()).getOperationId(), is("update"));
    }
}
