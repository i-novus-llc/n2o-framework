package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ChildrenToggleEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.FilterPositionEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.ElementIOFactory;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.io.cell.v2.CellIOv2;
import net.n2oapp.framework.config.io.control.v2.ControlIOv2;
import net.n2oapp.framework.config.io.fieldset.v4.FieldsetIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись таблицы
 */
@Component
public class TableElementIOV4 extends AbstractListWidgetElementIOv4<N2oTable> {
    private static final String WIDTH = "width";

    @Override
    public String getElementName() {
        return "table";
    }

    @Override
    public Class<N2oTable> getElementClass() {
        return N2oTable.class;
    }

    @Override
    public void io(Element e, N2oTable t, IOProcessor p) {
        super.io(e, t, p);
        p.attributeEnum(e, "selection", t::getSelection, t::setSelection, RowSelectionEnum.class);
        p.attribute(e, WIDTH, t::getWidth, t::setWidth);
        p.attribute(e, "height", t::getHeight, t::setHeight);
        p.attributeBoolean(e, "text-wrap", t::getTextWrap, t::setTextWrap);
        p.anyChildren(e, "columns", t::getColumns, t::setColumns, columns(p));
        p.child(e, null, "filters", t::getFilters, t::setFilters, N2oTable.N2oTableFilters::new, this::filters);
        p.attributeEnum(e, "children", t::getChildren, t::setChildren, ChildrenToggleEnum.class);
        p.merge(t, getElementName());
        t.adapterV4();
    }

    private void filters(Element e, N2oTable.N2oTableFilters f, IOProcessor p) {
        p.attributeEnum(e, "place", f::getPlace, f::setPlace, FilterPositionEnum.class);
        p.attribute(e, "default-values-query-id", f::getDefaultValuesQueryId, f::setDefaultValuesQueryId);
        p.attributeBoolean(e, "search-on-change", f::getFetchOnChange, f::setFetchOnChange);
        p.anyChildren(e, null, f::getItems, f::setItems, p.anyOf(SourceComponent.class), FieldsetIOv4.NAMESPACE, ControlIOv2.NAMESPACE);
    }

    private void abstractColumn(Element e, N2oAbstractColumn c, IOProcessor p) {
        p.attribute(e, "id", c::getId, c::setId);
        p.attribute(e, "src", c::getSrc, c::setSrc);
    }

    private void baseColumn(Element e, N2oBaseColumn c, IOProcessor p) {
        p.attribute(e, "class", c::getCssClass, c::setCssClass);
        p.attribute(e, "style", c::getStyle, c::setStyle);
        p.attribute(e, "text-field-id", c::getTextFieldId, c::setTextFieldId);
        p.attribute(e, "tooltip-field-id", c::getTooltipFieldId, c::setTooltipFieldId);
        p.attribute(e, "visible", c::getVisible, c::setVisible);
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attribute(e, "sorting-field-id", c::getSortingFieldId, c::setSortingFieldId);
        p.attributeEnum(e, "sorting-direction", c::getSortingDirection, c::setSortingDirection, SortingDirectionEnum.class);
        p.attribute(e, WIDTH, c::getWidth, c::setWidth);
        p.attributeBoolean(e, "resizable", c::getResizable, c::setResizable);
        p.attributeEnum(e, "fixed", c::getFixed, c::setFixed, ColumnFixedPositionEnum.class);
        p.anyChildren(e, "dependencies", c::getColumnVisibilities, c::setColumnVisibilities, p.oneOf(N2oBaseColumn.ColumnVisibility.class)
                .add("visibility", N2oBaseColumn.ColumnVisibility.class, this::dependency));
        p.anyAttributes(e, c::getExtAttributes, c::setExtAttributes);
    }

    private void dependency(Element e, N2oBaseColumn.ColumnVisibility t, IOProcessor p) {
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getModel, t::setModel, ReduxModelEnum.class);
        p.text(e, t::getValue, t::setValue);
    }

    private ElementIOFactory<N2oAbstractColumn, TypedElementReader<? extends N2oAbstractColumn>, TypedElementPersister<? super N2oAbstractColumn>> columns(IOProcessor p) {
        return p.oneOf(N2oAbstractColumn.class)
                .add("column", N2oSimpleColumn.class, this::simpleColumn)
                .add("filter-column", N2oFilterColumn.class, this::filterColumn)
                .add("multi-column", N2oMultiColumn.class, this::multiColumn);
    }

    private void simpleColumn(Element e, N2oSimpleColumn c, IOProcessor p) {
        abstractColumn(e, c, p);
        baseColumn(e, c, p);
        p.anyChild(e, null, c::getCell, c::setCell, p.anyOf(N2oCell.class).ignore("dependencies"), CellIOv2.NAMESPACE);
    }

    private void filterColumn(Element e, N2oFilterColumn c, IOProcessor p) {
        abstractColumn(e, c, p);
        baseColumn(e, c, p);
        p.anyChild(e, "filter", c::getFilter, c::setFilter, p.anyOf(N2oStandardField.class), ControlIOv2.NAMESPACE);
        p.anyChild(e, "cell", c::getCell, c::setCell, p.anyOf(N2oCell.class).ignore("filter"), CellIOv2.NAMESPACE);
    }

    private void multiColumn(Element e, N2oMultiColumn c, IOProcessor p) {
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "src", c::getSrc, c::setSrc);
        p.attribute(e, "class", c::getCssClass, c::setCssClass);
        p.attribute(e, "style", c::getStyle, c::setStyle);
        p.attribute(e, WIDTH, c::getWidth, c::setWidth);
        p.anyChildren(e, null, c::getChildren, c::setChildren, columns(p));
    }
}
