package net.n2oapp.framework.config.metadata.compile.cell;


import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.meta.cell.AbstractCell;
import net.n2oapp.framework.api.metadata.meta.cell.ListCell;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;


@Component
public class ListCellCompiler extends AbstractCellCompiler<ListCell, N2oListCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListCell.class;
    }

    @Override
    public ListCell compile(N2oListCell source, CompileContext<?, ?> context, CompileProcessor p) {
        ListCell listCell = new ListCell();
        build(listCell, source, context, p, property("n2o.api.cell.list.src"));
        listCell.setColor(source.getColor());
        listCell.setSize(castDefault(source.getSize(),
                () -> p.resolve(property("n2o.api.cell.list.size"), Integer.class)));
        listCell.setInline(castDefault(source.getInline(),
                () -> p.resolve(property("n2o.api.cell.list.inline"), Boolean.class)));
        listCell.setSeparator(source.getSeparator());
        if (source.getLabelFieldId() != null) {
            AbstractCell innerCell = p.compile(source.getCell() != null ? source.getCell() : new N2oTextCell(), context, p, new ComponentScope(source));
            innerCell.setFieldKey(source.getLabelFieldId());
            listCell.setContent(innerCell);
        }
        return listCell;
    }
}
