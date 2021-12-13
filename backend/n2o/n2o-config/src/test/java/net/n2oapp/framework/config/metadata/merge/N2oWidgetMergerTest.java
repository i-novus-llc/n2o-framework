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
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oCellsPack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тест слияния виджетов
 */
public class N2oWidgetMergerTest extends SourceMergerTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oActionsPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack())
                .ios(new FormElementIOV4(), new TableElementIOV4())
                .compilers(new FormCompiler(), new TableCompiler())
                .mergers(new N2oWidgetMerger<>(), new N2oFormMerger(), new N2oTableMerger());
    }

    @Test
    public void testMergeWidget() {
        N2oForm widget = merge("net/n2oapp/framework/config/metadata/local/merger/widget/parentWidgetForm.widget.xml",
                "net/n2oapp/framework/config/metadata/local/merger/widget/childWidgetForm.widget.xml")
                .get("parentWidgetForm", N2oForm.class);
        assertThat(widget, notNullValue());
        assertThat(widget.getDependsOn(), is("child"));
        assertThat(widget.getName(), is("Child"));
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
    public void testMergeForm() {
        N2oForm form = merge("net/n2oapp/framework/config/metadata/local/merger/widget/parentFormMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/local/merger/widget/childFormMerger.widget.xml")
                .get("parentFormMerger", N2oForm.class);
        assertThat(form.getMode(), is(FormMode.TWO_MODELS));
        assertThat(form.getPrompt(), is(true));
        assertThat(form.getDefaultValuesQueryId(), is("defQueryId"));

        SourceComponent[] items = form.getItems();
        assertThat(items.length, is(2));
        assertThat(((N2oInputSelect) items[0]).getId(), is("test2"));
        assertThat(((N2oInputText) items[1]).getId(), is("test1"));
    }

    @Test
    public void testMergeTable() {
        N2oTable table = merge("net/n2oapp/framework/config/metadata/local/merger/widget/parentTableMerger.widget.xml",
                "net/n2oapp/framework/config/metadata/local/merger/widget/childTableMerger.widget.xml")
                .get("parentTableMerger", N2oTable.class);
        assertThat(table.getSelection(), is(RowSelectionEnum.checkbox));
        assertThat(table.getHeight(), is("100px"));
        assertThat(table.getWidth(), is("200px"));
        assertThat(table.getTextWrap(), is(true));
        assertThat(table.getTableSize(), is(Size.lg));
        assertThat(table.getChildren(), is(N2oTable.ChildrenToggle.expand));
        assertThat(table.getSearchOnChange(), is(true));
        assertThat(table.getFiltersDefaultValuesQueryId(), is("test"));
        assertThat(table.getFilterPosition(), is(N2oTable.FilterPosition.left));

        AbstractColumn[] columns = table.getColumns();
        assertThat(columns.length, is(2));
        assertThat(columns[0].getTextFieldId(), is("test2"));
        assertThat(columns[1].getTextFieldId(), is("test1"));

        SourceComponent[] filters = table.getFilters();
        assertThat(columns.length, is(2));
        assertThat(((N2oInputSelect) filters[0]).getId(), is("test2"));
        assertThat(((N2oInputText) filters[1]).getId(), is("test1"));

        N2oPagination pagination = table.getPagination();
        assertThat(pagination.getFirst(), is(true));
        assertThat(pagination.getLast(), is(true));
        assertThat(pagination.getPrev(), is(true));
        assertThat(pagination.getNext(), is(true));
        assertThat(pagination.getShowSinglePage(), is(true));
        assertThat(pagination.getShowCount(), is(true));

        N2oRow rows = table.getRows();
        assertThat(rows.getRowClick().getActionId(), is("actionId"));
    }
}
