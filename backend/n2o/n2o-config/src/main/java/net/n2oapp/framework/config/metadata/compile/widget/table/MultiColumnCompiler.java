package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oAbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oMultiColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.MultiColumn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция заголовка мульти-столбца таблицы
 */
@Component
public class MultiColumnCompiler extends BaseColumnCompiler<N2oMultiColumn> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiColumn.class;
    }

    @Override
    public MultiColumn compile(N2oMultiColumn source, CompileContext<?, ?> context, CompileProcessor p) {
        MultiColumn compiled = new MultiColumn();
        compileBaseProperties(source, compiled, p);
        compiled.setLabel(source.getLabel());
        compiled.setMultiHeader(true);
        compiled.setChildren(new ArrayList<>());
        compiled.getElementAttributes().put("alignment", castDefault(source.getAlignment() == null ? null : source.getAlignment().getId(),
                () -> p.resolve(property("n2o.api.widget.column.multi.alignment"), String.class)));
        for (N2oAbstractColumn subColumn : source.getChildren()) {
            ((N2oBaseColumn) subColumn).setContentAlignment(castDefault(((N2oBaseColumn) subColumn).getContentAlignment(), source.getContentAlignment()));
            compiled.getChildren().add(p.compile(subColumn, context, p));
        }
        return compiled;
    }
}
