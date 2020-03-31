package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTooltipListCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись ячейки с тултипом и раскрывающимся текстовым списком
 */
@Component
public class TooltipListCellElementIOv2 extends AbstractCellElementIOv2<N2oTooltipListCell> {

    @Override
    public void io(Element e, N2oTooltipListCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "label-one", c::getOneLabel, c::setOneLabel);
        p.attribute(e, "label-few", c::getFewLabel, c::setFewLabel);
        p.attribute(e, "label-many", c::getManyLabel, c::setManyLabel);
        p.attributeEnum(e, "trigger", c::getTrigger, c::setTrigger, TriggerEnum.class);
    }

    @Override
    public Class<N2oTooltipListCell> getElementClass() {
        return N2oTooltipListCell.class;
    }

    @Override
    public String getElementName() {
        return "tooltip-list";
    }
}
