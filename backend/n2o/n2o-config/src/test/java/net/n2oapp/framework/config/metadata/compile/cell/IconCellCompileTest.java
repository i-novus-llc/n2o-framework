package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.IconCellElementIOv2;
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
 * Тест на компиляцию ячейки с иконкой
 */
public class IconCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new IconCellElementIOv2());
        builder.compilers(new IconCellCompiler());
    }

    @Test
    public void testIconCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testIconCell.widget.xml")
                .get(new WidgetContext("testIconCell"));

        N2oIconCell cell = (N2oIconCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("IconCell"));
        assertThat(cell.getIcon(), is("icon"));
        assertThat(cell.getIconType(), is(IconType.icon));

        cell = (N2oIconCell) table.getComponent().getCells().get(1);
        assertThat(cell.getSrc(), is("IconCell"));
        assertThat(cell.getIcon(), is("`type.id == 1 ? 'icon1' : type.id == 2 ? 'icon2' : 'icon3'`"));
        assertThat(cell.getIconType(), is(IconType.iconAndText));
        assertThat(cell.getText(), is("text"));
        assertThat(cell.getPosition(), is(Position.right));
        assertThat(cell.getTooltipFieldId(), is("tooltipId"));
    }
}

