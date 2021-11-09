package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.BadgeCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию ячейки со значком
 */
public class BadgeCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.ios(new BadgeCellElementIOv2());
        builder.compilers(new BadgeCellCompiler());
    }

    @Test
    public void testBadgeCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testBadgeCell.widget.xml")
                .get(new WidgetContext("testBadgeCell"));

        N2oBadgeCell cell = (N2oBadgeCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("BadgeCell"));
        assertThat(cell.getText(), is("text"));
        assertThat(cell.getFormat(), is("test"));
        assertThat(cell.getTextFormat(), is("test"));
        assertThat(cell.getColor(), is("`type.id == 1 ? 'success' : type.id == 2 ? 'danger' : 'info'`"));

        cell = (N2oBadgeCell) table.getComponent().getCells().get(1);
        assertThat(cell.getColor(), is("info"));
    }
}
