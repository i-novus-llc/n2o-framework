package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oBadgeCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oIconCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;
import net.n2oapp.framework.api.metadata.meta.cell.SwitchCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.SwitchCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oCellsPack;
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
 * Тест на компиляцию переключателя ячеек
 */
public class SwitchCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oCellsPack())
                .ios(new SwitchCellElementIOv2());
    }

    @Test
    public void testSwitchCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testSwitchCell.widget.xml")
                .get(new WidgetContext("testSwitchCell"));

        assertThat(table.getComponent().getCells().size(), is(1));
        assertThat(table.getComponent().getCells().get(0), instanceOf(SwitchCell.class));
        SwitchCell cell = (SwitchCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("SwitchCell"));
        assertThat(cell.getSwitchFieldId(), is("type.id"));

        // проверка, что вложенные ячейки скомпилировались
        assertThat(cell.getSwitchList().size(), is(2));
        assertThat(cell.getSwitchList().get("type1"), instanceOf(N2oBadgeCell.class));
        assertThat(cell.getSwitchList().get("type1").getSrc(), is("BadgeCell"));
        assertThat(((N2oBadgeCell) cell.getSwitchList().get("type1")).getText(), is("text"));
        assertThat(((N2oBadgeCell) cell.getSwitchList().get("type1")).getFormat(), is("format"));
        assertThat(cell.getSwitchList().get("type2"), instanceOf(N2oIconCell.class));
        assertThat(cell.getSwitchList().get("type2").getSrc(), is("IconCell"));
        assertThat(((N2oIconCell) cell.getSwitchList().get("type2")).getText(), is("text"));
        assertThat(((N2oIconCell) cell.getSwitchList().get("type2")).getIcon(), is("icon"));
        assertThat(((N2oIconCell) cell.getSwitchList().get("type2")).getPosition(), is(Position.right));

        // проверка default ячейки
        assertThat(cell.getSwitchDefault(), instanceOf(N2oTextCell.class));
        assertThat(cell.getSwitchDefault().getSrc(), is("TextCell"));
        assertThat(((N2oTextCell) cell.getSwitchDefault()).getFormat(), is("format"));
        assertThat(((N2oTextCell) cell.getSwitchDefault()).getSubTextFieldKey(), is("field1"));
    }
}
