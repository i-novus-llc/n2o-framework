package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oActionCell;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.config.metadata.validation.standard.IdValidationUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TableValidator implements SourceValidator<N2oTable>, SourceClassAware {

    @Override
    public void validate(N2oTable source, ValidateProcessor p) {
        IdValidationUtils.checkIds(source.getFilters(), p);
        if (source.getRows() != null && source.getRows().getRowClick() != null)
            p.validate(source.getRows().getRowClick().getAction());
        p.safeStreamOf(source.getColumns()).filter(abstractColumn -> abstractColumn instanceof N2oSimpleColumn).
                map(abstractColumn -> ((N2oSimpleColumn) abstractColumn).getCell()).collect(Collectors.toList()).
                stream().filter(n2oCell -> n2oCell instanceof N2oActionCell).
                map(actionCell -> ((N2oActionCell) actionCell).getN2oAction()).
                collect(Collectors.toList()).stream().forEach(p::validate);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTable.class;
    }
}
