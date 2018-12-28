package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oWidgetList;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChild;

@Component
public class WidgetListReaderV3 extends WidgetBaseXmlReaderV3<N2oWidget> {

    protected final static Namespace DEFAULT_CELL_NAMESPACE_URI = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-cell-1.0");

    @Override
    public String getElementName() {
        return "list";
    }

    @Override
    public N2oWidget read(Element element, Namespace namespace) {
        N2oWidgetList widgetList = new N2oWidgetList();
        readWidgetDefinition(element, namespace, widgetList);
        widgetList.setColumn(getChild(element, "rows", new TypedElementReader<N2oSimpleColumn>() {
            @Override
            public String getElementName() {
                return "rows";
            }

            @Override
            public N2oSimpleColumn read(Element element) {
                N2oSimpleColumn column = new N2oSimpleColumn();
                column.setTextFieldId(getAttributeString(element, "label-field-id"));
                List<Element> child = element.getChildren();
                if (child != null && child.size() > 0) {
                    N2oCell cell = (N2oCell) readerFactory.produce(child.get(0), element.getNamespace(), DEFAULT_CELL_NAMESPACE_URI).read(child.get(0));
                    column.setCell(cell);
                }
                return column;
            }

            @Override
            public Class<N2oSimpleColumn> getElementClass() {
                return N2oSimpleColumn.class;
            }
        }));
        return widgetList;
    }

    @Override
    public Class<N2oWidget> getElementClass() {
        return N2oWidget.class;
    }
}
