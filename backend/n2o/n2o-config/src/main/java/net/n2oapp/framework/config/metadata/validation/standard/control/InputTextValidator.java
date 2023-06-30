package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class InputTextValidator implements SourceValidator<N2oInputText>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInputText.class;
    }

    @Override
    public void validate(N2oInputText source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);

        if (source.getDefaultValue() != null && source.getDomain() != null)
            checkDomain(source, widgetScope);

        if (source.getStep() != null && source.getDomain() != null)
            checkStep(source, widgetScope);
    }

    private void checkDomain(N2oInputText source, WidgetScope widgetScope) {
        String message = "Значение 'default-value' не соответствует указанному 'domain=%s' для поля '%s' виджета %s";
        String domain = source.getDomain();
        if ("integer".equals(domain)) {
            ValidationUtils.checkInteger(source.getDefaultValue(), String.format(message, domain, source.getId(),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
        if ("short".equals(domain)) {
            ValidationUtils.checkShort(source.getDefaultValue(), String.format(message, domain, source.getId(),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
        if ("byte".equals(domain)) {
            ValidationUtils.checkByte(source.getDefaultValue(), String.format(message, domain, source.getId(),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
        if ("numeric".equals(domain)) {
            ValidationUtils.checkDouble(source.getDefaultValue(), String.format(message, domain, source.getId(),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
    }


    private void checkStep(N2oInputText source, WidgetScope widgetScope) {
        String message = "Значение 'step' не соответствует указанному 'domain=%s' для поля %s виджета %s";
        String domain = source.getDomain();
        if ("integer".equals(domain)) {
            ValidationUtils.checkInteger(source.getStep(), String.format(message, domain,
                    ValidationUtils.getIdOrEmptyString(source.getId()),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
        if ("short".equals(domain)) {
            ValidationUtils.checkShort(source.getStep(), String.format(message, domain, source.getId(),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
        if ("byte".equals(domain)) {
            ValidationUtils.checkByte(source.getStep(), String.format(message, domain, source.getId(),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
        if ("numeric".equals(domain)) {
            ValidationUtils.checkDouble(source.getStep(), String.format(message, domain, source.getId(),
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
        }
    }
}
