package net.n2oapp.framework.config.io.widget.table.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.ActionIOv1;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись ячейки с картинкой
 */
@Component
public class ImageCellElementIOv2 extends AbstractCellElementIOv2<N2oImageCell> {
    private Namespace actionDefaultNamespace = ActionIOv1.NAMESPACE;

    @Override
    public void io(Element e, N2oImageCell c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "url", c::getUrl, c::setUrl);
        p.attribute(e, "action-id", c::getActionId, c::setActionId);
        p.attribute(e, "width", c::getWidth, c::setWidth);
        p.attributeEnum(e, "shape", c::getShape, c::setShape, ImageShape.class);
        p.anyChild(e, null, c::getAction, c::setAction, p.anyOf(N2oAction.class), actionDefaultNamespace);
    }

    @Override
    public String getElementName() {
        return "image";
    }

    @Override
    public Class<N2oImageCell> getElementClass() {
        return N2oImageCell.class;
    }
}