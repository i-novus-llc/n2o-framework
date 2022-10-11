package net.n2oapp.framework.config.io.cell.v3;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oRatingCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись ячейки с рейтингом
 */
@Component
public class RatingCellElementIOv3 extends AbstractActionCellElementIOv3<N2oRatingCell> {
    @Override
    public void io(Element e, N2oRatingCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeBoolean(e, "show-tooltip", c::getShowTooltip, c::setShowTooltip);
        p.attributeBoolean(e, "half", c::getHalf, c::setHalf);
        p.attributeInteger(e, "max", c::getMax, c::setMax);
        p.attributeBoolean(e, "readonly", c::getReadonly, c::setReadonly);
    }

    @Override
    public String actionsSequenceTag() {
        return null;
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
