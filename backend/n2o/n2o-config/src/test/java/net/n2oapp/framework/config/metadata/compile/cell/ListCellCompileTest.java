package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oListCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.ListCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию ячейки со списком
 */
public class ListCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new ListCellElementIOv2());
        builder.compilers(new ListCellCompiler());
    }

    @Test
    public void testListCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testListCell.page.xml")
                .get(new PageContext("testListCell"));
        Table table = (Table) page.getWidget();
        N2oListCell cell = (N2oListCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("CollapsedCell"));
        assertThat(cell.getSize(), is(3));
        assertThat(cell.getColor(), is("color"));

        cell = (N2oListCell) table.getComponent().getCells().get(1);
        assertThat(cell.getSrc(), is("CollapsedCell"));
        assertThat(cell.getSize(), is(5));
        assertThat(cell.getColor(), is("`type.id == 1 ? 'success' : type.id == 2 ? 'danger' : 'info'`"));
        assertThat(cell.getLabelFieldId(), is("field"));
    }
}
