package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oSwitchCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SwitchCellValidator implements SourceValidator<N2oSwitchCell>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSwitchCell.class;
    }

    @Override
    public void validate(N2oSwitchCell source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (StringUtils.isBlank(source.getValueFieldId()))
            throw new N2oMetadataValidationException(String.format("Для ячейки <switch> виджета %s не указано значение 'value-field-id'",
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));

        Arrays.stream(source.getCases()).forEach(c -> {
            if (StringUtils.isBlank(c.getValue()))
                throw new N2oMetadataValidationException(String.format("Для <case> ячейки <switch> виджета %s не указано значение 'value'",
                        ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));

            if (c.getItem() != null)
                p.validate(c.getItem(), widgetScope);
        });

    }
}
