package net.n2oapp.framework.config.io.cell.v2;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oRatingCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись ячейки с рейтингом
 */
@Component
public class RatingCellElementIOv2 extends AbstractCellElementIOv2<N2oRatingCell> {
    @Override
    public void io(Element e, N2oRatingCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeBoolean(e, "show-tooltip", c::getShowTooltip, c::setShowTooltip);
        p.attributeBoolean(e, "half", c::getHalf, c::setHalf);
        p.attributeInteger(e, "max", c::getMax, c::setMax);
        p.attributeBoolean(e, "readonly", c::getReadonly, c::setReadonly);
        p.attribute(e, "action-id", c::getActionId, c::setActionId);
        p.anyChild(e, null, c::getAction, c::setAction, p.anyOf(N2oAction.class), ActionIOv1.NAMESPACE);
    }

    @Override
    public Class<N2oRatingCell> getElementClass() {
        return N2oRatingCell.class;
    }

    @Override
    public String getElementName() {
        return "rating";
    }
}
