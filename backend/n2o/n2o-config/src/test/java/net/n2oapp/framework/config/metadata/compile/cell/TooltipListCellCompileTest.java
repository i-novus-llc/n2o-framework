package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.meta.cell.TooltipListCell;
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
 * Тест на компиляцию ячейки с тултипом и раскрывающимся текстовым списком
 */
class TooltipListCellCompileTest extends SourceCompileTestBase {
    
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
    void testTooltipListCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testTooltipListCell.page.xml")
                .get(new PageContext("testTooltipListCell"));
        Table table = (Table) page.getWidget();
        TooltipListCell cell = (TooltipListCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("ListTextCell"));
        assertThat(cell.getFieldKey(), is("test1"));
        assertThat(cell.getFewLabel(), is("{size} объекта"));
        assertThat(cell.getManyLabel(), is("{size} объектов"));
        assertThat(cell.getDashedLabel(), is(false));
        assertThat(cell.getTrigger(), is(TriggerEnum.CLICK));

        cell = (TooltipListCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getFieldKey(), is("test2"));
        assertThat(cell.getLabel(), is("Объектов {size} шт"));
        assertThat(cell.getTrigger(), is(TriggerEnum.HOVER));
        assertThat(cell.getDashedLabel(), is(true));
    }
}
