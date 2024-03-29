package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oMultiColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция заголовка мульти-столбца таблицы
 */
@Component
public class MultiColumnHeaderCompiler extends AbstractHeaderCompiler<N2oMultiColumn> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiColumn.class;
    }

    @Override
    public ColumnHeader compile(N2oMultiColumn source, CompileContext<?, ?> context, CompileProcessor p) {
        ColumnHeader header = new ColumnHeader();
        compileBaseProperties(source, header, p);
        header.setLabel(source.getLabel());
        header.setMultiHeader(true);
        header.setChildren(new ArrayList<>());
        header.getElementAttributes().put("alignment", castDefault(source.getAlignment() == null ? null : source.getAlignment().getId(),
                () -> p.resolve(property("n2o.api.widget.column.multi.alignment"), String.class)));
        for (AbstractColumn subColumn : source.getChildren()) {
            subColumn.setContentAlignment(castDefault(subColumn.getContentAlignment(), source.getContentAlignment()));
            header.getChildren().add(p.compile(subColumn, context, p));
        }
        return header;
    }
}
