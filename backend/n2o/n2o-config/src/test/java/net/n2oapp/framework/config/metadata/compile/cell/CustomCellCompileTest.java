package net.n2oapp.framework.config.metadata.compile.cell;


import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCustomCell;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.CustomCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию настраиваемой ячейки
 */
public class CustomCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.ios(new CustomCellElementIOv2());
        builder.compilers(new CustomCellCompiler());
    }

    @Test
    public void testCustomCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testCustomCell.widget.xml")
                .get(new WidgetContext("testCustomCell"));

        N2oCustomCell cell = (N2oCustomCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("MyCell"));
        assertThat(cell.getAction(), instanceOf(LinkActionImpl.class));
    }
}
