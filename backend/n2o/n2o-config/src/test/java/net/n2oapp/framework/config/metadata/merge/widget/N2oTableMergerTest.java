package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.global.view.widget.table.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oAbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oBaseColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование слияния виджетов {@code <table>}
 */
class N2oTableMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oActionsIOV2Pack(), new N2oControlsV3IOPack(), new N2oCellsV3IOPack())
                .ios(new TableElementIOV5<>())
                .mergers(new N2oWidgetMerger<>(), new N2oTableMerger());
    }

    @Test
    void testMergeTable() {
        N2oTable table = merge("net/n2oapp/framework/config/metadata/merge/widget/parentToolbarMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childToolbarMerger.widget.xml")
                .get("parentToolbarMerger", N2oTable.class);
        N2oToolbar[] toolbars = table.getToolbars();
        assertThat(toolbars.length, is(2));
        assertThat(toolbars[0].getPlace(), is(PlaceEnum.TOP_RIGHT.getId()));
        assertThat(((N2oButton) toolbars[0].getItems()[0]).getLabel(), is("test1"));
        assertThat(toolbars[1].getPlace(), is(PlaceEnum.BOTTOM_CENTER.getId()));
        assertThat(((N2oButton) toolbars[1].getItems()[0]).getLabel(), is("test3"));
    }

    @Test
    void testSettingMergeTable() {
        N2oTable table = merge("net/n2oapp/framework/config/metadata/merge/widget/parentTableMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childTableMerger.widget.xml")
                .get("parentTableMerger", N2oTable.class);
        assertThat(table.getSelection(), is(RowSelectionEnum.CHECKBOX));
        assertThat(table.getHeight(), is("100px"));
        assertThat(table.getWidth(), is("200px"));
        assertThat(table.getTextWrap(), is(true));
        assertThat(table.getChildren(), is(ChildrenToggleEnum.EXPAND));
        assertThat(table.getFilters().getFetchOnChange(), is(true));
        assertThat(table.getFilters().getFetchOnEnter(), is(false));
        assertThat(table.getDatasourceId(), is("ds"));
        assertThat(table.getFilters().getDatasourceId(), is("ds_filter"));
        assertThat(table.getFilters().getPlace(), is(FilterPositionEnum.LEFT));
        assertThat(table.getStickyHeader(), is(false));
        assertThat(table.getStickyFooter(), is(true));
        assertThat(table.getScrollbarPosition(), is(ScrollbarPositionTypeEnum.TOP));

        N2oAbstractColumn[] columns = table.getColumns();
        assertThat(columns.length, is(2));
        assertThat(((N2oBaseColumn) columns[0]).getTextFieldId(), is("test2"));
        assertThat(((N2oBaseColumn) columns[1]).getTextFieldId(), is("test1"));

        SourceComponent[] filters = table.getFilters().getItems();
        assertThat(columns.length, is(2));
        assertThat(((N2oInputSelect) filters[0]).getId(), is("test2"));
        assertThat(((N2oInputText) filters[1]).getId(), is("test1"));

        N2oPagination pagination = table.getPagination();
        assertThat(pagination.getPrev(), is(true));
        assertThat(pagination.getNext(), is(true));
        assertThat(pagination.getShowLast(), is(true));
        assertThat(pagination.getShowCount(), is(ShowCountTypeEnum.NEVER));

        N2oRow rows = table.getRows();
        assertThat(rows.getRowClick().getActionId(), is("actionId"));
    }
}
