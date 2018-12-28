package net.n2oapp.framework.config.reader.widget.cell;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLink;
import org.jdom.Element;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * Считывает ячейку таблицы с ссылкой
 */
@Component
public class N2oLinkCellXmlReader extends AbstractN2oCellXmlReader<N2oLink> {

    @Override
    public String getElementName() {
        return "link";
    }

    @Override
    public N2oLink read(Element element) {
        if (element == null) return null;
        N2oLink res = new N2oLink();
        res.setId(getAttributeString(element, "id"));
        if (element.getChildren() != null && !element.getChildren().isEmpty()) {
            res.setAction((N2oAction) readerFactory.produce((Element) element.getChildren().get(0),
                    element.getNamespace(), DEFAULT_EVENT_NAMESPACE_URI).read((Element) element.getChildren().get(0)));
        }
        return res;
    }

    @Override
    public Class<N2oLink> getElementClass() {
        return N2oLink.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/n2o-cell-1.0";
    }
}
