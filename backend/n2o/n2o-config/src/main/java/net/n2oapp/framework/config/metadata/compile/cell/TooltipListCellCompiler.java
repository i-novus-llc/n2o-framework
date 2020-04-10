package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTooltipListCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки с тултипом и раскрывающимся текстовым списком
 */
@Component
public class TooltipListCellCompiler extends AbstractCellCompiler<N2oTooltipListCell, N2oTooltipListCell> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTooltipListCell.class;
    }

    @Override
    public N2oTooltipListCell compile(N2oTooltipListCell source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oTooltipListCell cell = new N2oTooltipListCell();
        build(cell, source, context, p, property("n2o.api.cell.tooltip_list.src"));
        cell.setLabel(source.getLabel());
        cell.setOneLabel(source.getOneLabel());
        cell.setFewLabel(source.getFewLabel());
        cell.setManyLabel(source.getManyLabel());
        cell.setDashedLabel(p.cast(source.getDashedLabel(), p.resolve(property("n2o.api.cell.tooltip_list.dashed_label"), Boolean.class)));
        cell.setTrigger(p.cast(source.getTrigger(), p.resolve(property("n2o.api.cell.tooltip_list.trigger"), TriggerEnum.class)));
        return cell;
    }
}
