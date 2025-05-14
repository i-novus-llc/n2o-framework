package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.TextCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v3.TextCellElementIOv3;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию ячейки с текстом
 */
class TextCellCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new TextCellElementIOv3());
        builder.compilers(new TextCellCompiler());
    }

    @Test
    void testTextCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testTextCell.page.xml")
                .get(new PageContext("testTextCell"));
        Table table = (Table) page.getWidget();
        TextCell cell = (TextCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("TextCell"));
        assertThat(cell.getFieldKey(), is("text"));
        assertThat(cell.getFormat(), is("0,0.00"));
        assertThat(cell.getSubTextFieldKey(), is("subtext"));
        assertThat(cell.getSubTextFormat(), is("date DD.MM.YYYY"));
        assertThat(cell.getTooltipFieldId(), is("tooltipId"));
        assertThat(cell.getIcon(), is("icon"));
        assertThat(cell.getIconPosition(), is(PositionEnum.LEFT));

        cell = (TextCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getSrc(), is("TextCell"));
        assertThat(cell.getFieldKey(), is("text2"));
        assertThat(cell.getFormat(), is("0,0.00"));
        assertThat(cell.getSubTextFieldKey(), is("subtext2"));
        assertThat(cell.getSubTextFormat(), is("date DD.MM.YYYY"));
        assertThat(cell.getElementAttributes().get("className"), is("`test2 == 1 ? 'a' : test2 == 2 ? 'b' : test2 == 3 ? 'c' : null`"));
        assertThat(cell.getIconPosition(), is(PositionEnum.RIGHT));
    }
}
