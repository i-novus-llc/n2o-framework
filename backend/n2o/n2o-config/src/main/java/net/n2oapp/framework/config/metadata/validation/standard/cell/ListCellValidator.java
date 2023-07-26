package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.compile.enums.Color;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

@Component
public class ListCellValidator implements SourceValidator<N2oListCell>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListCell.class;
    }

    @Override
    public void validate(N2oListCell source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getN2oSwitch() != null) {
            if (org.apache.commons.lang3.StringUtils.isBlank(source.getN2oSwitch().getValueFieldId()))
                throw new N2oMetadataValidationException(String.format("Для конструкции <switch> ячейки <list> виджета %s не указано значение 'value-field-id'",
                        ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));

            if (source.getN2oSwitch().getCases() != null)
                p.safeStreamOf(source.getN2oSwitch().getCases().keySet()).forEach(c -> {
                    if (c == null)
                        throw new N2oMetadataValidationException(String.format("Для <case> конструкции <switch> ячейки <list> виджета %s не указано значение 'value'",
                                ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
                });
        }

        if (source.getColor() != null && !StringUtils.isLink(source.getColor()) &&
                !EnumUtils.isValidEnum(Color.class, source.getColor())) {
            throw new N2oMetadataValidationException(
                    String.format("В ячейке <list> виджета %s указано недопустимое значение атрибута color=\"%s\"",
                            ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId()), source.getColor()));
        }
    }
}