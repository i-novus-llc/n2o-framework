package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Трансформатор доступа строки таблицы
 */
@Component
public class TableAccessTransformer extends BaseAccessTransformer<Table, CompileContext<?, ?>> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Table.class;
    }

    @Override
    public Table transform(Table compiled, CompileContext context, CompileProcessor p) {
        if (compiled.getComponent() != null && compiled.getComponent().getBody().getRow() != null &&
                compiled.getComponent().getBody().getRow().getClick() != null) {
            transfer(compiled.getComponent().getBody().getRow().getClick().getAction(),
                    compiled.getComponent().getBody().getRow());
        }
        transferColumnSecurityToCells(compiled);
        return compiled;
    }

    private void transferColumnSecurityToCells(Table compiled) {
        if (compiled.getComponent() == null)
            return;
        List<AbstractColumn> columns = compiled.getComponent().getHeader().getCells();
        List<Cell> cells = compiled.getComponent().getBody().getCells();
        if (columns == null || cells == null)
            return;
        for (int i = 0; i < columns.size() && i < cells.size(); i++) {
            AbstractColumn column = columns.get(i);
            Cell cell = cells.get(i);
            if (column instanceof PropertiesAware columnWithProps && cell instanceof PropertiesAware cellWithProps) {
                transfer(columnWithProps, cellWithProps);
            }
        }
    }
}
