package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.script.ScriptProcessor;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция кастомной ячейки
 */
@Component
public class TextCellCompiler extends AbstractCellCompiler<N2oTextCell, N2oTextCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTextCell.class;
    }

    @Override
    public N2oTextCell compile(N2oTextCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oTextCell cell = new N2oTextCell();
        build(cell, source, context, p, property("n2o.default.cell.text.src"));
        cell.setCssClass(p.cast(source.getCssClass(), ScriptProcessor.buildExpressionForSwitch(source.getClassSwitch())));
        cell.setFormat(source.getFormat());
        return cell;
    }
}
