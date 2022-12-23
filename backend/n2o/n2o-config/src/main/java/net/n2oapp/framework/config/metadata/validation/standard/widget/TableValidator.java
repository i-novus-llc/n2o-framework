package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TableValidator implements SourceValidator<N2oTable>, SourceClassAware {

    @Override
    public void validate(N2oTable source, SourceProcessor p) {
        MetaActions actions = new MetaActions(
                p.safeStreamOf(source.getActions()).collect(Collectors.toMap(ActionBar::getId, Function.identity()))
        );
        WidgetScope widgetScope = new WidgetScope(source.getId(), source.getDatasourceId(), source.getDatasource(), actions);

        if (source.getRows() != null && source.getRows().getRowClick() != null)
            Arrays.stream(source.getRows().getRowClick().getActions()).forEach(item -> p.validate(item, widgetScope));
        p.safeStreamOf(source.getColumns())
                .filter(N2oSimpleColumn.class::isInstance)
                .map(abstractColumn -> ((N2oSimpleColumn) abstractColumn).getCell())
                .filter(N2oActionCell.class::isInstance)
                .flatMap(actionCell -> p.safeStreamOf(((N2oActionCell) actionCell).getActions()))
                .forEach(item -> p.validate(item, widgetScope));
        p.safeStreamOf(source.getFilters()).forEach(item -> p.validate(item, widgetScope));
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
