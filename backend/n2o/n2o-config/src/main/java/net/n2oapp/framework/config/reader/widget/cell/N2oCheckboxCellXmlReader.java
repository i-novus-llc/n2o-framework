package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.config.reader.tools.ConditionReaderV1;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChild;

/**
 * @author rgalina
 * @since 01.03.2016
 */
@Component
public class N2oCheckboxCellXmlReader extends AbstractN2oCellXmlReader<N2oCheckboxCell> {
    @Override
    public String getElementName() {
        return "checkbox";
    }

    @Override
    public N2oCheckboxCell read(Element element) {
        if (element == null) return null;
        Element el = element.getChild("invoke-action", element.getNamespace());
        if (el == null)
            return new N2oCheckboxCell();
        N2oCheckboxCell checkbox = new N2oCheckboxCell();
        checkbox.setAction(new N2oInvokeAction(getAttributeString(el, "action-id")));
        return checkbox;
    }

    @Override
    public Class<N2oCheckboxCell> getElementClass() {
        return N2oCheckboxCell.class;
    }
}
