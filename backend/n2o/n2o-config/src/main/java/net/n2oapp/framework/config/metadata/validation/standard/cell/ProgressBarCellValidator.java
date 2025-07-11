package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.isInvalidColor;

@Component
public class ProgressBarCellValidator implements SourceValidator<N2oProgressBarCell>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oProgressBarCell.class;
    }

    @Override
    public void validate(N2oProgressBarCell source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (isInvalidColor(source.getColor())) {
            throw new N2oMetadataValidationException(
                    String.format("В ячейке <progress> виджета %s указано недопустимое значение атрибута color=\"%s\"",
                            ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId()), source.getColor()));
        }
    }
}
