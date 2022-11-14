package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TableValidator implements SourceValidator<N2oTable>, SourceClassAware {

    @Override
    public void validate(N2oTable source, SourceProcessor p) {
        if (source.getRows() != null && source.getRows().getRowClick() != null)
            Arrays.stream(source.getRows().getRowClick().getActions()).forEach(p::validate);
        p.safeStreamOf(source.getColumns())
                .filter(N2oSimpleColumn.class::isInstance)
                .map(abstractColumn -> ((N2oSimpleColumn) abstractColumn).getCell())
                .filter(N2oActionCell.class::isInstance)
                .flatMap(actionCell -> p.safeStreamOf(((N2oActionCell) actionCell).getActions()))
                .forEach(p::validate);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
