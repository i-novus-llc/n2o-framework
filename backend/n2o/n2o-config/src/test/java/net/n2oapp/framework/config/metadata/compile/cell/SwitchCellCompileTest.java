package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.api.metadata.meta.cell.BadgeCell;
import net.n2oapp.framework.api.metadata.meta.cell.IconCell;
import net.n2oapp.framework.api.metadata.meta.cell.SwitchCell;
import net.n2oapp.framework.api.metadata.meta.cell.TextCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.SwitchCellElementIOv2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oCellsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


/**
 * Тест на компиляцию переключателя ячеек
 */
public class SwitchCellCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
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
    void testSwitchCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testSwitchCell.page.xml")
                .get(new PageContext("testSwitchCell"));
        Table table = (Table) page.getWidget();
        assertThat(table.getComponent().getCells().size(), is(1));
        assertThat(table.getComponent().getCells().get(0), instanceOf(SwitchCell.class));
        SwitchCell cell = (SwitchCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("SwitchCell"));
        assertThat(cell.getSwitchFieldId(), is("type.id"));

        // проверка, что вложенные ячейки скомпилировались
        assertThat(cell.getSwitchList().size(), is(2));
        assertThat(cell.getSwitchList().get("type1"), instanceOf(BadgeCell.class));
        assertThat(cell.getSwitchList().get("type1").getSrc(), is("BadgeCell"));
        assertThat(((BadgeCell) cell.getSwitchList().get("type1")).getText(), is("text"));
        assertThat(((BadgeCell) cell.getSwitchList().get("type1")).getFormat(), is("format"));
        assertThat(cell.getSwitchList().get("type2"), instanceOf(IconCell.class));
        assertThat(cell.getSwitchList().get("type2").getSrc(), is("IconCell"));
        assertThat(((IconCell) cell.getSwitchList().get("type2")).getText(), is("text"));
        assertThat(((IconCell) cell.getSwitchList().get("type2")).getIcon(), is("icon"));
        assertThat(((IconCell) cell.getSwitchList().get("type2")).getPosition(), is(Position.RIGHT));

        // проверка default ячейки
        assertThat(cell.getSwitchDefault(), instanceOf(TextCell.class));
        assertThat(cell.getSwitchDefault().getSrc(), is("TextCell"));
        assertThat(((TextCell) cell.getSwitchDefault()).getFormat(), is("format"));
        assertThat(((TextCell) cell.getSwitchDefault()).getSubTextFieldKey(), is("field1"));
    }
}
