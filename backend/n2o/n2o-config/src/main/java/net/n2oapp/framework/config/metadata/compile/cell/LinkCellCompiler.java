package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLink;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки иконка
 */
@Component
public class LinkCellCompiler extends AbstractCellCompiler<N2oLink, N2oLink> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLink.class;
    }

    @Override
    public N2oLink compile(N2oLink source, CompileContext<?,?> context, CompileProcessor p) {
        N2oLink cell = new N2oLink();
        build(cell, source, context, p, property("n2o.default.cell.link.src"));
        compileAction(cell, source, context, p);
        cell.setIcon(p.resolveJS(source.getIcon()));
        if (source.getIcon() != null) {
            cell.setType(p.cast(source.getType(), IconType.text));
        }
        return cell;
    }
}
