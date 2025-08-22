package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ChildrenToggle;
import net.n2oapp.framework.api.metadata.global.view.widget.table.FilterPosition;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.RowSelectionEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.io.ElementIOFactory;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.persister.TypedElementPersister;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.io.cell.v3.CellIOv3;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import net.n2oapp.framework.config.io.fieldset.v5.FieldsetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись таблицы версии 5.0
 */
@Component
public class TableElementIOV5<T extends N2oTable> extends AbstractListWidgetElementIOv5<T> {

    @Override
    public String getElementName() {
        return "table";
    }

    @Override
    public Class getElementClass() {
        return N2oTable.class;
    }

    @Override
    public void io(Element e, T t, IOProcessor p) {
        super.io(e, t, p);
        p.attributeEnum(e, "selection", t::getSelection, t::setSelection, RowSelectionEnum.class);
        p.attributeBoolean(e, "auto-select", t::getAutoSelect, t::setAutoSelect);
        p.attribute(e, "width", t::getWidth, t::setWidth);
        p.attribute(e, "height", t::getHeight, t::setHeight);
        p.attributeBoolean(e, "text-wrap", t::getTextWrap, t::setTextWrap);
        p.anyChildren(e, "columns", t::getColumns, t::setColumns, columns(p));
        p.child(e, null, "filters", t::getFilters, t::setFilters, N2oTable.N2oTableFilters::new, this::filters);
        p.attributeEnum(e, "children", t::getChildren, t::setChildren, ChildrenToggle.class);
    }

    private void filters(Element e, N2oTable.N2oTableFilters f, IOProcessor p) {
        p.attributeEnum(e, "place", f::getPlace, f::setPlace, FilterPosition.class);
        p.attributeBoolean(e, "fetch-on-change", f::getFetchOnChange, f::setFetchOnChange);
        p.attributeBoolean(e, "fetch-on-clear", f::getFetchOnClear, f::setFetchOnClear);
        p.attributeBoolean(e, "fetch-on-enter", f::getFetchOnEnter, f::setFetchOnEnter);
        p.attribute(e, "datasource", f::getDatasourceId, f::setDatasourceId);
        p.anyChildren(e,null, f::getItems, f::setItems, p.anyOf(SourceComponent.class), FieldsetIOv5.NAMESPACE, ControlIOv3.NAMESPACE);
    }

    private void abstractColumn(Element e, AbstractColumn c, IOProcessor p) {
        baseProperties(e, c, p);
        p.attribute(e, "id", c::getId, c::setId);
        p.attribute(e, "text-field-id", c::getTextFieldId, c::setTextFieldId);
        p.attribute(e, "tooltip-field-id", c::getTooltipFieldId, c::setTooltipFieldId);
        p.attribute(e, "icon", c::getIcon, c::setIcon);
        p.attribute(e, "sorting-field-id", c::getSortingFieldId, c::setSortingFieldId);
        p.attributeEnum(e, "sorting-direction", c::getSortingDirection, c::setSortingDirection, SortingDirection.class);
        p.attribute(e, "width", c::getWidth, c::setWidth);
        p.attributeBoolean(e, "resizable", c::getResizable, c::setResizable);
        p.attributeEnum(e, "fixed", c::getFixed, c::setFixed, ColumnFixedPosition.class);
        p.anyChildren(e, "dependencies", c::getColumnVisibilities, c::setColumnVisibilities, p.oneOf(AbstractColumn.ColumnVisibility.class)
                .add("visibility", AbstractColumn.ColumnVisibility.class, this::dependency));
        p.anyAttributes(e, c::getExtAttributes, c::setExtAttributes);
    }

    private void dependency(Element e, AbstractColumn.ColumnVisibility t, IOProcessor p) {
        p.attribute(e, "datasource", t::getDatasourceId, t::setDatasourceId);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

    private ElementIOFactory<AbstractColumn, TypedElementReader<? extends AbstractColumn>, TypedElementPersister<? super AbstractColumn>> columns(IOProcessor p) {
        return p.oneOf(AbstractColumn.class)
                .add("column", N2oSimpleColumn.class, this::column)
                .add("filter-column", N2oFilterColumn.class, this::filterColumn)
                .add("multi-column", N2oMultiColumn.class, this::multiColumn);
    }

    private void column(Element e, N2oSimpleColumn c, IOProcessor p) {
        abstractColumn(e, c, p);
        p.anyChild(e, null, c::getCell, c::setCell, p.anyOf(N2oCell.class).ignore("dependencies"), CellIOv3.NAMESPACE);
    }

    private void filterColumn(Element e, N2oFilterColumn c, IOProcessor p) {
        abstractColumn(e, c, p);
        p.anyChild(e, "filter", c::getFilter, c::setFilter, p.anyOf(N2oStandardField.class), ControlIOv3.NAMESPACE);
        p.anyChild(e, "cell", c::getCell, c::setCell, p.anyOf(N2oCell.class).ignore("filter"), CellIOv3.NAMESPACE);
    }

    private void multiColumn(Element e, N2oMultiColumn c, IOProcessor p) {
        baseProperties(e, c, p);
        p.anyChildren(e, null, c::getChildren, c::setChildren, columns(p));
    }

    private void baseProperties(Element e, AbstractColumn c, IOProcessor p) {
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "src", c::getSrc, c::setSrc);
        p.attribute(e, "class", c::getCssClass, c::setCssClass);
        p.attribute(e, "style", c::getStyle, c::setStyle);
        p.attribute(e, "visible", c::getVisible, c::setVisible);
        p.attribute(e, "width", c::getWidth, c::setWidth);
        p.attributeEnum(e, "alignment", c::getAlignment, c::setAlignment, Alignment.class);
        p.attributeEnum(e, "content-alignment", c::getContentAlignment, c::setContentAlignment, Alignment.class);
    }
}
