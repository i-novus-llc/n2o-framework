package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oToolbarCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ToolbarCellValidator implements SourceValidator<N2oToolbarCell>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oToolbarCell.class;
    }

    @Override
    public void validate(N2oToolbarCell source, SourceProcessor p) {
        if (source.getItems() != null)
            Arrays.stream(source.getItems()).forEach(p::validate);

        checkEmptyToolbar(source, p);
    }

    private static void checkEmptyToolbar(N2oToolbarCell source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getGenerate() == null && source.getItems() == null)
            throw new N2oMetadataValidationException(
                    String.format("Не заданы элементы или атрибут 'generate' в <toolbar> ячейке виджета %s",
                            ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));
    }
}
