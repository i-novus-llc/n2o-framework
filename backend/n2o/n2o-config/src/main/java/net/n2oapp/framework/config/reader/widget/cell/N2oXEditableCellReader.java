package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oXEditableCell;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author V. Alexeev.
 * @date 29.03.2016
 */
@Component
public class N2oXEditableCellReader extends AbstractN2oCellXmlReader<N2oXEditableCell> {

    @Override
    public String getElementName() {
        return "x-editable";
    }

    @Override
    public N2oXEditableCell read(Element element) {

        if (element == null) return null;
        String actionId = ReaderJdomUtil.getAttributeString(element, "action-id");
        List<Element> child = element.getChildren();
        if(child.size() == 0) return null;
        N2oField field = (N2oField) readerFactory.produce(child.get(0)).read(child.get(0));
        N2oXEditableCell cell = new N2oXEditableCell();
        cell.setAction(new N2oInvokeAction(actionId));
        cell.setN2oField(field);
        cell.setFormat(ReaderJdomUtil.getAttributeString(element, "format"));
        return cell;
    }

    @Override
    public Class<N2oXEditableCell> getElementClass() {
        return N2oXEditableCell.class;
    }

}
