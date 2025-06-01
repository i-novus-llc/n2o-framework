package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;
import net.n2oapp.framework.api.metadata.meta.cell.LinkCell;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
/**
 * Компиляция ячейки иконка
 */
@Component
public class LinkCellCompiler extends AbstractCellCompiler<LinkCell, N2oLinkCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLinkCell.class;
    }

    @Override
    public LinkCell compile(N2oLinkCell source, CompileContext<?,?> context, CompileProcessor p) {
        LinkCell cell = new LinkCell();
        build(cell, source, p, property("n2o.api.cell.link.src"));
        if (source.getUrl() == null) {
            compileAction(cell, source, context, p);
        } else {
            cell.setUrl(StringUtils.hasLink(source.getUrl())
                    ? p.resolveJS(source.getUrl())
                    : RouteUtil.normalize(source.getUrl()));
            TargetEnum defaultTarget = RouteUtil.isApplicationUrl(source.getUrl()) ? TargetEnum.APPLICATION : TargetEnum.SELF;
            cell.setTarget(castDefault(source.getTarget(), defaultTarget));
        }

            cell.setIcon(p.resolveJS(source.getIcon()));

        return cell;
    }
}
