package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCheckboxCell;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.CheckboxCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тест на компиляцию ячейки с чекбоксом
 */
public class CheckboxCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.ios(new CheckboxCellElementIOv2());
        builder.compilers(new CheckboxCellCompiler());
    }

    @Test
    public void testCheckboxCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testCheckboxCell.widget.xml")
                .get(new WidgetContext("testCheckboxCell"));

        N2oCheckboxCell cell = (N2oCheckboxCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("CheckboxCell"));
        assertThat(cell.getEnabled(), nullValue());
        assertThat(cell.getActionId(), is("update"));

        cell = (N2oCheckboxCell) table.getComponent().getCells().get(1);
        assertThat(cell.getEnabled(), is("false"));

        cell = (N2oCheckboxCell) table.getComponent().getCells().get(2);
        assertThat(cell.getEnabled(), nullValue());
        assertThat(cell.getAction(), instanceOf(LinkActionImpl.class));
    }
}
