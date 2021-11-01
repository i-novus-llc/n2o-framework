package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.TextCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию ячейки с текстом
 */
public class TextCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new TextCellElementIOv2());
        builder.compilers(new TextCellCompiler());
    }

    @Test
    public void testTextCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testTextCell.widget.xml")
                .get(new WidgetContext("testTextCell"));

        N2oTextCell cell = (N2oTextCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("TextCell"));
        assertThat(cell.getFieldKey(), is("text"));
        assertThat(cell.getFormat(), is("0,0.00"));
        assertThat(cell.getSubTextFieldKey(), is("subtext"));
        assertThat(cell.getSubTextFormat(), is("date DD.MM.YYYY"));
        assertThat(cell.getTooltipFieldId(), is("tooltipId"));

        cell = (N2oTextCell) table.getComponent().getCells().get(1);
        assertThat(cell.getSrc(), is("TextCell"));
        assertThat(cell.getFieldKey(), is("text2"));
        assertThat(cell.getFormat(), is("0,0.00"));
        assertThat(cell.getSubTextFieldKey(), is("subtext2"));
        assertThat(cell.getSubTextFormat(), is("date DD.MM.YYYY"));
        assertThat(cell.getCssClass(), is("`test2 == 1 ? 'a' : test2 == 2 ? 'b' : test2 == 3 ? 'c' : null`"));
    }

}
