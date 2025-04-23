package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.cell.CheckboxCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.CheckboxCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тест на компиляцию ячейки с чекбоксом
 */
class CheckboxCellCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
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
    void testCheckboxCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testCheckboxCell.page.xml")
                .get(new PageContext("testCheckboxCell"));
        Table table = (Table) page.getWidget();
        CheckboxCell cell = (CheckboxCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("CheckboxCell"));
        assertThat(cell.getDisabled(), nullValue());

        cell = (CheckboxCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getDisabled(), is("true"));

        cell = (CheckboxCell) table.getComponent().getBody().getCells().get(2);
        assertThat(cell.getDisabled(), nullValue());
        assertThat(cell.getAction(), instanceOf(LinkActionImpl.class));
    }
}
