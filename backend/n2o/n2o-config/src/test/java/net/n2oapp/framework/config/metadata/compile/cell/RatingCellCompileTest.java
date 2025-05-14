package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.cell.RatingCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.RatingCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию ячейки с рейтингом
 */
class RatingCellCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(), new N2oAllDataPack());
        builder.ios(new RatingCellElementIOv2());
        builder.compilers(new RatingCellCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/cell/test.object.xml"));
    }

    @Test
    void testRatingCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testRatingCell.page.xml")
                .get(new PageContext("testRatingCell"));
        Table table = (Table) page.getWidget();
        RatingCell cell = (RatingCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("RatingCell"));
        assertThat(cell.getHalf(), is(true));
        assertThat(cell.getMax(), is(10));
        assertThat(cell.getShowTooltip(), is(true));
        assertThat(cell.getReadonly(), is(false));
        assertThat(cell.getAction(), instanceOf(InvokeAction.class));

        cell = (RatingCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getSrc(), is("RatingCell"));
        assertThat(cell.getHalf(), is(false));
        assertThat(cell.getMax(), is(5));
        assertThat(cell.getShowTooltip(), is(false));
        assertThat(cell.getReadonly(), is(true));

        cell = (RatingCell) table.getComponent().getBody().getCells().get(2);
        assertThat(cell.getAction(), instanceOf(InvokeAction.class));
    }
}
