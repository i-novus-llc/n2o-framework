package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class IconCellValidator implements SourceValidator<N2oIconCell>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oIconCell.class;
    }

    @Override
    public void validate(N2oIconCell source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getIconSwitch() != null) {
            if (StringUtils.isBlank(source.getIconSwitch().getValueFieldId()))
                throw new N2oMetadataValidationException(String.format("Для конструкции <switch> ячейки <icon> виджета %s не указано значение 'value-field-id'",
                        ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));

            if (source.getIconSwitch().getCases() != null)
                p.safeStreamOf(source.getIconSwitch().getCases().keySet()).forEach(c -> {
                    if (c == null)
                        throw new N2oMetadataValidationException(String.format("Для <case> конструкции <switch> ячейки <icon> виджета %s не указано значение 'value'",
                                ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
                });
        }
    }
}
