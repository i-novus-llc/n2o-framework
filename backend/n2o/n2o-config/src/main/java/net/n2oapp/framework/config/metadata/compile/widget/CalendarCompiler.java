package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCalendar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.Calendar;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarViewType;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarWidgetComponent;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция виджета календарь
 */
@Component
public class CalendarCompiler extends BaseWidgetCompiler<Calendar, N2oCalendar> {
    @Override
    protected String getPropertyWidgetSrc() {
        return "n2o.api.widget.calendar.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCalendar.class;
    }

    @Override
    public Calendar compile(N2oCalendar source, CompileContext<?, ?> context, CompileProcessor p) {
        Calendar calendar = new Calendar();
        N2oDatasource datasource = initInlineDatasource(calendar, source, p);
        CompiledObject object = getObject(source, datasource, p);
        compileBaseWidget(calendar, source, context, p, object);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModel.resolve, p.getScope(PageScope.class));

        CalendarWidgetComponent component = calendar.getComponent();
        component.setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.widget.calendar.size"), Integer.class)));
        component.setHeight(source.getHeight());
        String dateDomain = p.resolve(property("n2o.api.control.datetime.domain"), String.class);
        component.setDate((Date) p.resolve(source.getDefaultDate(), dateDomain));
        component.setDefaultView(source.getDefaultView() != null ?
                source.getDefaultView().getTitle() :
                p.resolve(property("n2o.api.widget.calendar.view"), String.class));
        component.setViews(Arrays.stream(source.getViews()).map(v -> CalendarViewType.valueOf(v).getTitle()).toArray(String[]::new));
        component.setMinTime(p.resolveJS(source.getMinTime()));
        component.setMaxTime(p.resolveJS(source.getMaxTime()));
        component.setMarkDaysOff(p.cast(source.getMarkDaysOff(),
                p.resolve(property("n2o.api.widget.calendar.mark_days_off"), Boolean.class)));
        component.setSelectable(p.cast(source.getSelectable(),
                p.resolve(property("n2o.api.widget.calendar.selectable"), Boolean.class)));
        component.setStep(p.cast(source.getStep(), p.resolve(property("n2o.api.widget.calendar.step"), Integer.class)));
        component.setTimeSlots(p.cast(source.getTimeSlotCount(),
                p.resolve(property("n2o.api.widget.calendar.time_slot_count"), Integer.class)));
        component.setTitleFieldId(source.getTitleFieldId());
        component.setTooltipFieldId(source.getTooltipFieldId());
        component.setStartFieldId(source.getStartFieldId());
        component.setEndFieldId(source.getEndFieldId());
        component.setCellColorFieldId(source.getCellColorFieldId());
        component.setDisabledFieldId(source.getDisabledFieldId());
        component.setResourceFieldId(source.getResourcesFieldId());
        component.setResources(source.getResources());
        if (source.getActionOnSelectSlot() != null)
            component.setOnSelectSlot(p.compile(source.getActionOnSelectSlot(), context, object, new ComponentScope(source), widgetScope));
        if (source.getActionOnSelectEvent() != null)
            component.setOnSelectEvent(p.compile(source.getActionOnSelectEvent(), context, object, new ComponentScope(source), widgetScope));
        component.setFormats(source.getFormats());

        return calendar;
    }
}
