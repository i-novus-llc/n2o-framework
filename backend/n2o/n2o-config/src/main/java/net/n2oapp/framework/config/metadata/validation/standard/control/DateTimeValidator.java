package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.StringUtils.isLink;

@Component
public class DateTimeValidator implements SourceValidator<N2oDatePicker>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDatePicker.class;
    }

    @Override
    public void validate(N2oDatePicker source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        checkDefaultValue(source, widgetScope.getWidgetId());
    }

    private void checkDefaultValue(N2oDatePicker source, String widgetId) {
        if (source.getDefaultValue() != null && !isLink(source.getDefaultValue()))
            ValidationUtils.checkDate(source.getDefaultValue(),
                    String.format("Значение 'default-value' поля %s виджета %s должно иметь формат yyyy-MM-dd HH:mm:ss",
                            ValidationUtils.getIdOrEmptyString(source.getId()),
                            ValidationUtils.getIdOrEmptyString(widgetId)));
    }
}
