package net.n2oapp.framework.config.reader.widget;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.event.action.N2oOpenPage;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldsetRow;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCustomWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTree;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oTable;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oColorCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oLinkCell;

import java.util.Arrays;

/**
 * Created by enuzhdina on 18.05.2015.
 */
public abstract class BaseWidgetReaderTest {

    protected void assertWidgetAttribute(N2oWidget widget) {
        assert widget.getName().equals("test");
        assert widget.getSize().equals(32);
        assert widget.getSrc().equals("test");
        assert widget.getCssClass().equals("test");
        assert widget.getCustomize().equals("css");
        assert widget.getQueryId().equals("blank");
    }

    protected void assertStandardForm(N2oForm form) {
        NamespaceUriAware field = ((N2oFieldsetRow) ((N2oFieldSet) form.getItems()[0]).getItems()[0]).getItems()[0];
        assert !(field instanceof N2oField) || ((N2oField) field).getId().equals("id");
    }

    protected void assertStandardTable(N2oTable table) {
        assert table.getColumns().length == 13;
        for (AbstractColumn column : table.getColumns()) {
            assert column.getTextFieldId().equals("id");
            assert column.getTooltipFieldId().equals("id");
            assert column.getFormat().equals("date DD.MM.YYYY");
            assert column.getWidth().equals("100");
        }

        //проверяем чек-бокс
        N2oCheckboxCell checkBoxCell = (N2oCheckboxCell) ((N2oSimpleColumn) table.getColumns()[0]).getCell();
        assert checkBoxCell.getAction() != null;

        //проверяем цвет и иконку
        for (N2oSwitch object : Arrays.asList(((N2oColorCell) ((N2oSimpleColumn) table.getColumns()[1]).getCell()).getStyleSwitch(), ((N2oIconCell) ((N2oSimpleColumn) table.getColumns()[2]).getCell()).getIconSwitch())) {
            assert object.getFieldId().equals("id");
            assert object.getCases().get("key").equalsIgnoreCase("value");
            assert object.getDefaultCase().equals("test");
        }

        //провеяем link
        N2oLinkCell linkCell = (N2oLinkCell) ((N2oSimpleColumn) table.getColumns()[3]).getCell();
        assert ((N2oAnchor) linkCell.getAction()).getTarget().name().equals("newWindow");
        assert ((N2oAnchor) linkCell.getAction()).getHref().equals("https://www.google.ru/");

        N2oLinkCell linkCell2 = (N2oLinkCell) ((N2oSimpleColumn) table.getColumns()[4]).getCell();
        N2oAbstractPageAction openPage = (N2oOpenPage) linkCell2.getAction();
        assert openPage.getPageId().equals("test");
        assert openPage.getOperationId().equals("create");
        assert openPage.getMasterFieldId().equals("id");
        assert openPage.getDetailFieldId().equals("id");
        assert openPage.getContainerId().equals("test");
        assert openPage.getResultContainerId().equals("test");
        assert openPage.getRefreshOnClose().equals(true);
        assert openPage.getWidth().equals("100");
        assert openPage.getPreFilters()[0].getFieldId().equals("id");
        assert openPage.getPreFilters()[0].getTargetWidgetId().equals("test");
        assert openPage.getPreFilters()[0].getValue().equals("{test}");

        N2oLinkCell linkCell3 = (N2oLinkCell) ((N2oSimpleColumn) table.getColumns()[5]).getCell();
        N2oAbstractPageAction showModal = (N2oShowModal) linkCell3.getAction();

        assert showModal.getPageId().equals("test");
        assert showModal.getOperationId().equals("create");
        assert showModal.getMasterFieldId().equals("id");
        assert showModal.getDetailFieldId().equals("id");
        assert showModal.getContainerId().equals("test");
        assert showModal.getResultContainerId().equals("test");
        assert showModal.getRefreshOnClose().equals(true);
        assert showModal.getWidth().equals("100");
        assert showModal.getPreFilters()[0].getFieldId().equals("id");
        assert showModal.getPreFilters()[0].getTargetWidgetId().equals("test");
        assert showModal.getPreFilters()[0].getValue().equals("{test}");

        assert table.getFilterOpened().equals(true);
        assert table.getFilterPosition().name().toLowerCase().equals("left");

        NamespaceUriAware field = ((N2oFieldSet) ((N2oFieldSet) table.getFilters()[0]).getItems()[1]).getItems()[0];
        assert !(field instanceof N2oField) || ((N2oField) field).getId().equals("id");

        assert table.getColumns()[0].getSortingDirection().toString().toLowerCase().equals("asc");

        assert table.getRows().getColor().getCases().get("key").equals("value");
        assert table.getRows().getColor().getDefaultCase().equals("test");
    }

    private void assertBaseTree(N2oTree tree) {
        assert tree.getAjax().equals(true);
        assert tree.getCheckboxes().equals(true);
    }

    protected void assertInheritanceTree(N2oTree tree) {
        assertBaseTree(tree);
        assert tree.getParentFieldId().equals("id");
        assert tree.getLabelFieldId().equals("id");
        assert tree.getHasChildrenFieldId().equals("id");
    }

    protected void assertCustomWidget(N2oCustomWidget custom) {
        assert custom.getSrc().equals("test");
    }

    protected void assertRefWidget(N2oPage page) {
        assert page.getWidgets().size() == 3;
        assert page.getWidgets().get(0).getId().equals("form");
        assert page.getWidgets().get(1).getId().equals("table");
        assert page.getWidgets().get(2).getId().equals("tree");
        assert page.getWidgets().get(0).getRefId().equals("testFormReader");
        assert page.getWidgets().get(1).getRefId().equals("testTableReader");
        assert page.getWidgets().get(2).getRefId().equals("testTreeReader1");
    }

    protected void assertFieldSetAttribute(N2oForm form) {
        N2oFieldSet fieldSet = (N2oFieldSet) form.getItems()[0];
        assert fieldSet.getDependencyCondition().equals("test");
        assert fieldSet.getFieldLabelLocation().name().toLowerCase().equals("left");
        assert fieldSet.getLabel().equals("test");
        assert ((N2oFieldsetRow) fieldSet.getItems()[0]).getClass().equals(N2oFieldsetRow.class);
        NamespaceUriAware field = ((N2oFieldsetRow) fieldSet.getItems()[0]).getItems()[0];
        assert !(field instanceof N2oField) || ((N2oField) field).getId().equals("id");
    }
}
