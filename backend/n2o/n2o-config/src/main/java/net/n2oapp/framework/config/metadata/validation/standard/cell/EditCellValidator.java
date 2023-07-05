package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oEditCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class EditCellValidator  implements SourceValidator<N2oEditCell>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oEditCell.class;
    }

    @Override
    public void validate(N2oEditCell source, SourceProcessor p) {
        if (source.getN2oField() == null)
            throw new N2oMetadataValidationException(String.format("У ячейки <edit> виджета %s не задано поле ввода",
                    ValidationUtils.getIdOrEmptyString(p.getScope(WidgetScope.class).getWidgetId())));
    }
}
