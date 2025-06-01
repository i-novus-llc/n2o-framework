package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCalendar;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.Calendar;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarViewTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.calendar.CalendarWidgetComponent;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

import static net.n2oapp.framework.api.StringUtils.prepareSizeAttribute;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        compileBaseWidget(calendar, source, context, p);
        N2oAbstractDatasource datasource = getDatasourceById(source.getDatasourceId(), p);
        CompiledObject object = getObject(source, datasource, p);
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), ReduxModelEnum.RESOLVE, p);

        CalendarWidgetComponent component = calendar.getComponent();
        component.setSize(castDefault(source.getSize(), () -> p.resolve(property("n2o.api.widget.calendar.size"), Integer.class)));
        component.setHeight(prepareSizeAttribute(source.getHeight()));
        String dateDomain = p.resolve(property("n2o.api.control.date_time.domain"), String.class);
        component.setDate((Date) p.resolve(source.getDefaultDate(), dateDomain));
        component.setDefaultView(source.getDefaultView() != null ?
                source.getDefaultView().getTitle() :
                p.resolve(property("n2o.api.widget.calendar.view"), String.class));
        component.setViews(Arrays.stream(source.getViews())
                .flatMap(view -> Arrays.stream(CalendarViewTypeEnum.values()).filter(value -> value.getId().equals(view)).map(CalendarViewTypeEnum::getTitle))
                .toArray(String[]::new));
        component.setMinTime(p.resolveJS(source.getMinTime()));
        component.setMaxTime(p.resolveJS(source.getMaxTime()));
        component.setMarkDaysOff(castDefault(source.getMarkDaysOff(),
                () -> p.resolve(property("n2o.api.widget.calendar.mark_days_off"), Boolean.class)));
        component.setSelectable(castDefault(source.getSelectable(),
                () -> p.resolve(property("n2o.api.widget.calendar.selectable"), Boolean.class)));
        component.setStep(castDefault(source.getStep(), () -> p.resolve(property("n2o.api.widget.calendar.step"), Integer.class)));
        component.setTimeSlots(castDefault(source.getTimeSlotCount(),
                () -> p.resolve(property("n2o.api.widget.calendar.time_slot_count"), Integer.class)));
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
