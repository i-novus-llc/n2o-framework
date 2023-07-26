package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetColumn;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

@Component
public class FieldSetColumnValidator implements SourceValidator<N2oFieldsetColumn>, SourceClassAware {

    @Override
    public void validate(N2oFieldsetColumn source, SourceProcessor p) {
        if(source.getSize()!=null && (source.getSize() < 1 || source.getSize() > 12))
            throw new N2oMetadataValidationException(String.format("Размер колонки филдсета виджета %s должен иметь значение от 1 до 12",
                    ValidationUtils.getIdOrEmptyString(p.getScope(WidgetScope.class).getWidgetId())));
        if (ArrayUtils.isEmpty(source.getItems()) && source.getSize() == null)
            throw new N2oMetadataValidationException(String.format("Для <сol> виджета %s необходимо задать поля, либо же атрибут 'size'",
                    ValidationUtils.getIdOrEmptyString(p.getScope(WidgetScope.class).getWidgetId())));
        p.safeStreamOf(source.getItems()).forEach(p::validate);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetColumn.class;
    }
}
