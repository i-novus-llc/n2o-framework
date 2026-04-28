package net.n2oapp.framework.config.metadata.compile.widget.table;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.control.InputSelect;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.control.ValidationTypeEnum;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.DndColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.RowClick;
import net.n2oapp.framework.api.metadata.meta.widget.table.SimpleColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;
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
        builder.getEnvironment().getContextProcessor().set("ctxParam", "testValue");
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
        SimplePage page = (SimplePage) pipeline.get(context, new DataSet().add("param", "1"));
        RowClick rowClick = ((TableWidgetComponent) page.getWidget().getComponent()).getBody().getRow().getClick();
        assertThat(((LinkAction) rowClick.getAction()).getUrl(), is("/p/w/1/row/p_w_row_w1_row"));
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
                .getFirst().getRows().getFirst().getCols().getFirst().getFields().getFirst()).getControl();
        assertThat(select.getDataProvider().getQueryMapping().get("name").getValue(), is("1"));
    }

    /**
     * Проверка резолва контекстных переменных в условиях кнопки тулбара (PerformButtonBinder)
     */
    @Test
    void performButtonConditionResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind(
                "net/n2oapp/framework/config/metadata/compile/page/testTableConditionBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml");
        SimplePage page = (SimplePage) pipeline.get(new PageContext("testTableConditionBinder"), new DataSet());
        Table<?> table = (Table<?>) page.getWidget();
        PerformButton button = (PerformButton) table.getToolbar().get("topLeft").getFirst().getButtons().getFirst();
        assertThat(button.getConditions().get(ValidationTypeEnum.VISIBLE).getFirst().getExpression(),
                is("status == 'testValue'"));
    }

    /**
     * Проверка резолва контекстных переменных в условиях видимости колонки (AbstractColumnBinder)
     */
    @Test
    void columnConditionResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind(
                "net/n2oapp/framework/config/metadata/compile/page/testTableConditionBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml");
        SimplePage page = (SimplePage) pipeline.get(new PageContext("testTableConditionBinder"), new DataSet());
        TableWidgetComponent component = ((Table<?>) page.getWidget()).getComponent();
        SimpleColumn column = (SimpleColumn) component.getHeader().getCells().getFirst();
        assertThat(column.getConditions().get(ValidationTypeEnum.VISIBLE).getFirst().getExpression(),
                is("status == 'testValue'"));
    }

    /**
     * Проверка резолва контекстных переменных в условиях видимости дочерних колонок dnd-column (DndColumnBinder)
     */
    @Test
    void dndColumnChildrenConditionResolve() {
        ReadCompileBindTerminalPipeline pipeline = bind(
                "net/n2oapp/framework/config/metadata/compile/page/testTableConditionBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/query/testEmptyBody.query.xml");
        SimplePage page = (SimplePage) pipeline.get(new PageContext("testTableConditionBinder"), new DataSet());
        TableWidgetComponent component = ((Table<?>) page.getWidget()).getComponent();
        DndColumn dndColumn = (DndColumn) component.getHeader().getCells().get(1);
        SimpleColumn childColumn = dndColumn.getChildren().getFirst();
        assertThat(childColumn.getConditions().get(ValidationTypeEnum.VISIBLE).getFirst().getExpression(),
                is("status == 'testValue'"));
    }
}
