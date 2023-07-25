package net.n2oapp.framework.config.metadata.validation.standard.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

@Component
public class FieldSetRowValidator implements SourceValidator<N2oFieldsetRow>, SourceClassAware {

    @Override
    public void validate(N2oFieldsetRow source, SourceProcessor p) {
        if (ArrayUtils.isEmpty(source.getItems()))
            throw new N2oMetadataValidationException(String.format("Для <row> виджета %s необходимо задать поля",
                    ValidationUtils.getIdOrEmptyString(p.getScope(WidgetScope.class).getWidgetId())));
        p.safeStreamOf(source.getItems()).forEach(p::validate);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldsetRow.class;
    }
}
