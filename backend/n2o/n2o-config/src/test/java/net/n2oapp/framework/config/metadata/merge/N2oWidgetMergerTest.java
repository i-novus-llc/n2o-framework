package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.list.N2oInputSelect;
import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.api.metadata.global.view.widget.FormMode;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.table.*;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v4.FormElementIOV4;
import net.n2oapp.framework.config.io.widget.v4.TableElementIOV4;
import net.n2oapp.framework.config.io.widget.v5.TableElementIOV5;
import net.n2oapp.framework.config.metadata.merge.widget.N2oFormMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oTableMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oWidgetMerger;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oCellsPack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.jdom2.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест слияния виджетов
 */
public class N2oWidgetMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oActionsPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack())
                .ios(new FormElementIOV4(), new TableElementIOV4(), new TableElementIOV5())
                .mergers(new N2oWidgetMerger<>(), new N2oFormMerger(), new N2oTableMerger());
    }

    @Test
    void testMergeWidget() {
        N2oForm widget = merge("net/n2oapp/framework/config/metadata/merge/widget/parentWidgetForm.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childWidgetForm.widget.xml")
                .get("parentWidgetForm", N2oForm.class);
        assertThat(widget, notNullValue());
        assertThat(widget.getDependsOn(), is("child"));
        assertThat(widget.getQueryId(), is("parent"));
        assertThat(widget.getObjectId(), is("parent"));
        assertThat(widget.getPreFilters().length, is(2));
        assertThat(widget.getVisible(), is("true"));
        assertThat(widget.getActions().length, is(2));

        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-1.0"))).get("extAttr1"), is("child1"));
        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("ext", "http://example.com/n2o/ext-1.0"))).get("extAttr2"), is("child2"));

        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("extChild", "http://example.com/n2o/ext-child"))).get("extAttr"), is("child"));
        assertThat(widget.getExtAttributes().get(new N2oNamespace(Namespace.getNamespace("extParent", "http://example.com/n2o/ext-parent"))).get("extAttr"), is("parent"));
    }

    @Test
    void testMergeForm() {
        N2oForm form = merge("net/n2oapp/framework/config/metadata/merge/widget/parentFormMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childFormMerger.widget.xml")
                .get("parentFormMerger", N2oForm.class);
        assertThat(form.getMode(), is(FormMode.TWO_MODELS));
        assertThat(form.getUnsavedDataPrompt(), is(true));
        assertThat(form.getDefaultValuesQueryId(), is("defQueryId"));

        SourceComponent[] items = form.getItems();
        assertThat(items.length, is(2));
        assertThat(((N2oInputSelect) items[0]).getId(), is("test2"));
        assertThat(((N2oInputText) items[1]).getId(), is("test1"));
    }

    @Test
    void testMergeTable() {
        N2oTable table = merge("net/n2oapp/framework/config/metadata/merge/widget/parentTableMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childTableMerger.widget.xml")
                .get("parentTableMerger", N2oTable.class);
        assertThat(table.getSelection(), is(RowSelectionEnum.CHECKBOX));
        assertThat(table.getHeight(), is("100px"));
        assertThat(table.getWidth(), is("200px"));
        assertThat(table.getTextWrap(), is(true));
        assertThat(table.getChildren(), is(ChildrenToggle.EXPAND));
        assertThat(table.getFilters().getFetchOnChange(), is(true));
        assertThat(table.getFilters().getDatasource().getQueryId(), is("test"));
        assertThat(table.getFilters().getPlace(), is(FilterPosition.LEFT));

        AbstractColumn[] columns = table.getColumns();
        assertThat(columns.length, is(2));
        assertThat(columns[0].getTextFieldId(), is("test2"));
        assertThat(columns[1].getTextFieldId(), is("test1"));

        SourceComponent[] filters = table.getFilters().getItems();
        assertThat(columns.length, is(2));
        assertThat(((N2oInputSelect) filters[0]).getId(), is("test2"));
        assertThat(((N2oInputText) filters[1]).getId(), is("test1"));

        N2oPagination pagination = table.getPagination();
        assertThat(pagination.getPrev(), is(true));
        assertThat(pagination.getNext(), is(true));
        assertThat(pagination.getShowLast(), is(true));
        assertThat(pagination.getShowCount(), is(ShowCountType.ALWAYS));

        N2oRow rows = table.getRows();
        assertThat(rows.getRowClick().getActionId(), is("actionId"));
    }

    @Test
    void testMergeTableV5() {
        N2oTable table = merge("net/n2oapp/framework/config/metadata/merge/widget/parentTableV5Merger.widget.xml",
                "net/n2oapp/framework/config/metadata/merge/widget/childTableV5Merger.widget.xml")
                .get("parentTableV5Merger", N2oTable.class);
        assertThat(table.getSelection(), is(RowSelectionEnum.CHECKBOX));
        assertThat(table.getHeight(), is("100px"));
        assertThat(table.getWidth(), is("200px"));
        assertThat(table.getTextWrap(), is(true));
        assertThat(table.getChildren(), is(ChildrenToggle.EXPAND));
        assertThat(table.getFilters().getFetchOnChange(), is(true));
        assertThat(table.getFilters().getFetchOnEnter(), is(false));
        assertThat(table.getDatasourceId(), is("ds"));
        assertThat(table.getFilters().getDatasourceId(), is("ds_filter"));
        assertThat(table.getFilters().getPlace(), is(FilterPosition.LEFT));

        AbstractColumn[] columns = table.getColumns();
        assertThat(columns.length, is(2));
        assertThat(columns[0].getTextFieldId(), is("test2"));
        assertThat(columns[1].getTextFieldId(), is("test1"));

        SourceComponent[] filters = table.getFilters().getItems();
        assertThat(columns.length, is(2));
        assertThat(((N2oInputSelect) filters[0]).getId(), is("test2"));
        assertThat(((N2oInputText) filters[1]).getId(), is("test1"));

        N2oPagination pagination = table.getPagination();
        assertThat(pagination.getPrev(), is(true));
        assertThat(pagination.getNext(), is(true));
        assertThat(pagination.getShowLast(), is(true));
        assertThat(pagination.getShowCount(), is(ShowCountType.NEVER));

        N2oRow rows = table.getRows();
        assertThat(rows.getRowClick().getActionId(), is("actionId"));
    }
}
