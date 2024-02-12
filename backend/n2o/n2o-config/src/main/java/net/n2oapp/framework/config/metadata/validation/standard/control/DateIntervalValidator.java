package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.isLink;

@Component
public class DateIntervalValidator implements SourceValidator<N2oDateInterval>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDateInterval.class;
    }

    @Override
    public void validate(N2oDateInterval source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        checkDate(source.getBegin(), "begin", source.getId(), widgetScope.getWidgetId());
        checkDate(source.getEnd(), "end", source.getId(), widgetScope.getWidgetId());
        checkDate(source.getMin(), "min", source.getId(), widgetScope.getWidgetId());
        checkDate(source.getMax(), "max", source.getId(), widgetScope.getWidgetId());
    }

    private void checkDate(String date, String attribute, String sourceId, String widgetId) {
        if (date != null && !isLink(date))
            ValidationUtils.checkDate(date, String.format("Значение '%s' поля %s виджета %s должно иметь формат yyyy-MM-dd HH:mm:ss или yyyy-MM-dd",
                    attribute, ValidationUtils.getIdOrEmptyString(sourceId), ValidationUtils.getIdOrEmptyString(widgetId)));
    }
}
