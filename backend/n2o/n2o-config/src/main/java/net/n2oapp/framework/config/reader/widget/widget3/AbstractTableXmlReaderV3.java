package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oAbstractTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oRow;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.DirectionType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.DynamicColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.widget.SwitchReader;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;


/**
 * Астрактная реализация считывания таблицы (версии виджетов 3.0) из xml файла в source-модель n2o
 * Считывает только общие для таблиц свойства.
 * @param <T>  source-модель таблицы n2o
 */
public abstract class AbstractTableXmlReaderV3<T extends N2oAbstractTable> extends WidgetBaseXmlReaderV3<N2oWidget> {

    protected final static Namespace DEFAULT_CELL_NAMESPACE_URI = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-cell-1.0");
    
    protected void getAbstractTableDefinition(Element tableElement, Namespace namespace, N2oAbstractTable n2oTable,
                                              NamespaceReaderFactory extensionReaderFactory) {
        readRef(tableElement, n2oTable);
        Element columnsElement = tableElement.getChild("columns", namespace);
        if (columnsElement != null) {
            n2oTable.setColumns(readColumns(columnsElement, namespace, extensionReaderFactory));
        }

        Element sortingsElement = tableElement.getChild("sortings", namespace);
        if (sortingsElement != null) {
            List<Element> sortings = sortingsElement.getChildren();
            for (Element sorting : sortings) {
                String sortingFieldId = getAttributeString(sorting, "sorting-field-id");
                DirectionType direction = getAttributeEnum(sorting, "direction",
                       DirectionType.class);
                for (AbstractColumn column : n2oTable.getColumns()) {
                    if (column.getTextFieldId().equals(sortingFieldId)){
                        column.setSortingDirection(direction);
                        continue;
                    }
                }
            }
        }

        Element rowsElement = tableElement.getChild("rows", namespace);
        if (rowsElement != null) {
            Element colorElement = rowsElement.getChild("color", namespace);
            if (colorElement != null) {
                N2oRow rows = new N2oRow();
                rows.setColor(readSwitchCell(colorElement, namespace, "color-field-id"));
                n2oTable.setRows(rows);
            }
        }
        n2oTable.setPagingMode(getElementAttributeEnum(tableElement, "paging", "mode", N2oAbstractTable.PagingMode.class));
        n2oTable.setAlwaysShowCount(getElementAttributeBoolean(tableElement, "paging", "always-show-count"));

        n2oTable.setHasCheckboxes(getElementBoolean(tableElement, "checkboxes"));
        n2oTable.setAutoSelect(getElementBoolean(tableElement, "auto-select"));

        readWidgetDefinition(tableElement, namespace, n2oTable);
    }

    private static N2oSwitch readSwitchCell(Element icon, Namespace namespace, String fieldIdAttributeName) {
        if (icon == null)
            return null;
        return new SwitchReader(fieldIdAttributeName).read(icon);
    }

    @Override
    public Class<N2oWidget> getElementClass() {
        return N2oWidget.class;
    }

    public static AbstractColumn[] readColumns(Element element, Namespace namespace, NamespaceReaderFactory extensionReaderFactory) {
        List<Element> columns = element.getChildren();
        return columns.stream().map(columnElement -> {
            boolean isDynamic = columnElement.getName().equals("dynamic-column");
            AbstractColumn column = isDynamic ? new DynamicColumn() : new N2oSimpleColumn();
            String columnFieldId = columnElement.getAttribute("column-field-id").getValue();
            String tooltipFieldId = null;
            Attribute tooltipFieldIdElement = columnElement.getAttribute("tooltip-field-id");
            if (tooltipFieldIdElement != null)
                tooltipFieldId = tooltipFieldIdElement.getValue();
            String width = null;
            Attribute widthElement = columnElement.getAttribute("width");
            if (widthElement != null)
                width = widthElement.getValue();

            column.setTextFieldId(columnFieldId);
            column.setTooltipFieldId(tooltipFieldId);
            column.setWidth(width);
            column.setFormat(getAttributeString(columnElement, "format"));
            column.setLabelName(getAttributeString(columnElement, "name"));
            column.setLabelType(getAttributeEnum(columnElement, "type", LabelType.class));
            column.setLabelIcon(getAttributeString(columnElement, "icon"));
            column.setVisible(getAttributeString(columnElement, "visible"));
            column.setVisibilityCondition(getAttributeString(columnElement, "visibility-condition"));

            if (isDynamic) {
                readDynamicColumn(columnElement, column, namespace, extensionReaderFactory);
            } else {
                readSimpleColumn(columnElement, column, namespace, extensionReaderFactory);
            }

            return column;
        }).toArray(AbstractColumn[]::new);

    }

    private static void readSimpleColumn(Element element, AbstractColumn c, Namespace namespace,
                                         NamespaceReaderFactory extensionReaderFactory) {
        N2oSimpleColumn column = ((N2oSimpleColumn) c);
        if (element.getChildren() != null && element.getChildren().size() > 0) {
            N2oCell cell = (N2oCell) extensionReaderFactory.produce((Element) element.getChildren().get(0), element.getNamespace(),
                    DEFAULT_CELL_NAMESPACE_URI).read((Element) element.getChildren().get(0));
            column.setCell(cell);
        }
    }

    private static void readDynamicColumn(Element element, AbstractColumn c, Namespace namespace,
                                          NamespaceReaderFactory extensionReaderFactory) {
        Element aSwitch = element.getChild("switch", namespace);
        if (aSwitch == null) {
            return;
        }
        DynamicColumn column = ((DynamicColumn) c);
        column.setN2oSwitch(new DynamicSwitchReader<>(extensionReaderFactory, DEFAULT_CELL_NAMESPACE_URI.getURI(), N2oCell.class).read(element));
    }
}
