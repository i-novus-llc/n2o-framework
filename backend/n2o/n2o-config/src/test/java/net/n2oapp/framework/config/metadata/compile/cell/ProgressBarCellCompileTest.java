package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;
import net.n2oapp.framework.api.metadata.meta.cell.ProgressBarCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию ячейки с индикатором прогресса
 */
class ProgressBarCellCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testProgressBarCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testProgressBarCell.page.xml")
                .get(new PageContext("testProgressBarCell"));
        Table table = (Table) page.getWidget();
        ProgressBarCell cell = (ProgressBarCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("ProgressBarCell"));
        assertThat(cell.getActive(), is(true));
        assertThat(cell.getColor(), is("info"));
        assertThat(cell.getSize(), is(N2oProgressBarCell.SizeEnum.LARGE));
        assertThat(cell.getStriped(), is(true));

        cell = (ProgressBarCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getSrc(), is("ProgressBarCell"));
        assertThat(cell.getSize(), is(N2oProgressBarCell.SizeEnum.NORMAL));
        assertThat(cell.getActive(), is(false));
        assertThat(cell.getStriped(), is(false));
    }
}
