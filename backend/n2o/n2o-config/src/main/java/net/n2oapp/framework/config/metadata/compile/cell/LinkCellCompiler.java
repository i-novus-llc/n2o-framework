package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки иконка
 */
@Component
public class LinkCellCompiler extends AbstractCellCompiler<N2oLinkCell, N2oLinkCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLinkCell.class;
    }

    @Override
    public N2oLinkCell compile(N2oLinkCell source, CompileContext<?,?> context, CompileProcessor p) {
        N2oLinkCell cell = new N2oLinkCell();
        build(cell, source, context, p, property("n2o.api.cell.link.src"));
        if (source.getUrl() == null) {
            compileAction(cell, source, context, p);
            if (cell.getCompiledAction() != null && cell.getCompiledAction() instanceof LinkAction) {
                LinkAction linkAction = ((LinkAction) cell.getCompiledAction());
                cell.setActionId(null);
                cell.setUrl(linkAction.getUrl());
                cell.setTarget(linkAction.getTarget());
                cell.setPathMapping(linkAction.getPathMapping());
                cell.setQueryMapping(linkAction.getQueryMapping());
            }
        } else {
            cell.setUrl(p.resolveJS(source.getUrl()));
            Target defaultTarget = RouteUtil.isApplicationUrl(source.getUrl()) ? Target.application : Target.self;
            cell.setTarget(p.cast(source.getTarget(), defaultTarget));
        }
        cell.setIcon(p.resolveJS(source.getIcon()));
        if (source.getIcon() != null) {
            cell.setType(p.cast(source.getType(), IconType.text));
        }
        return cell;
    }
}
