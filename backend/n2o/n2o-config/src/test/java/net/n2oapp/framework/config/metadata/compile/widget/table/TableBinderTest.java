package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TableBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oAllDataPack(),
                new N2oPagesPack(),
                new N2oWidgetsPack(),
                new N2oRegionsPack(),
                new N2oActionsPack(),
                new N2oCellsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack()
        );
    }

    /**
     * резолв PathParameter у rowClick
     */
    @Test
    void rowClickResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/page/testTableRowClick.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml");
        PageContext context = new PageContext("testTableRowClick", "/p/w/:param/row");
        TableWidgetComponent component = (TableWidgetComponent) ((SimplePage) pipeline.get(context, new DataSet().add("param", "1"))).getWidget().getComponent();
        assertThat(component.getBody().getRow().getClick().getUrl(), is("/p/w/1/row/p_w_row_w1_row"));
        assertThat(component.getBody().getRow().getClick().getPathMapping().isEmpty(), is(true));
    }

    /**
     * Подстановка значения пре-фильтра селекта в фильтрах таблицы
     */
    @Test
    void testFilters() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/page/testTableFiltersBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testTableFilter.query.xml");
        PageContext context = new PageContext("testTableFiltersBinder", "/:id/");
        Table component = (Table) ((SimplePage) pipeline.get(context, new DataSet().add("id", "1"))).getWidget();
        InputSelect select = (InputSelect) ((StandardField) component.getFilter().getFilterFieldsets()
                .get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getControl();
        assertThat(select.getDataProvider().getQueryMapping().get("name").getValue(), is("1"));
    }
}
