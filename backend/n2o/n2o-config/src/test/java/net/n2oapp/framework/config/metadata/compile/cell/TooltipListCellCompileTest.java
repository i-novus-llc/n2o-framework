package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTooltipListCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.TooltipListCellElementIOv2;
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
 * Тест на компиляцию ячейки с тултипом и раскрывающимся текстовым списком
 */
public class TooltipListCellCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new TooltipListCellElementIOv2());
        builder.compilers(new TooltipListCellCompiler());
    }

    @Test
    public void testTooltipListCell() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/cell/testTooltipListCell.widget.xml")
                .get(new WidgetContext("testTooltipListCell"));

        N2oTooltipListCell cell = (N2oTooltipListCell) table.getComponent().getCells().get(0);
        assertThat(cell.getSrc(), is("ListTextCell"));
        assertThat(cell.getFieldKey(), is("test1"));
        assertThat(cell.getFewLabel(), is("{size} объекта"));
        assertThat(cell.getManyLabel(), is("{size} объектов"));
        assertThat(cell.getDashedLabel(), is(false));
        assertThat(cell.getTrigger(), is(TriggerEnum.click));

        cell = (N2oTooltipListCell) table.getComponent().getCells().get(1);
        assertThat(cell.getFieldKey(), is("test2"));
        assertThat(cell.getLabel(), is("Объектов {size} шт"));
        assertThat(cell.getTrigger(), is(TriggerEnum.hover));
        assertThat(cell.getDashedLabel(), is(true));
    }
}
