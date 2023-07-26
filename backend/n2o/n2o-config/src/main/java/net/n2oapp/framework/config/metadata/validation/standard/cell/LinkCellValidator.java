package net.n2oapp.framework.config.metadata.validation.standard.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LinkCellValidator implements SourceValidator<N2oLinkCell>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLinkCell.class;
    }

    @Override
    public void validate(N2oLinkCell source, SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (source.getUrl() != null && (source.getActionId() != null || source.getActions() != null))
            throw new N2oMetadataValidationException(String.format("В ячейке <link> виджета %s одновременно указаны 'url', 'action-id' либо действие",
                    ValidationUtils.getIdOrEmptyString(widgetScope.getWidgetId())));

        if (source.getActions() !=  null)
            Arrays.stream(source.getActions()).forEach(p::validate);
    }
}
