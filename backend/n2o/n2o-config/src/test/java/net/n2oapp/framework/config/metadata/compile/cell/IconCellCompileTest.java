package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.IconCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v2.IconCellElementIOv2;
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
 * Тест на компиляцию ячейки с иконкой
 */
class IconCellCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
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
    void testIconCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testIconCell.page.xml")
                .get(new PageContext("testIconCell"));
        Table table = (Table) page.getWidget();
        IconCell cell = (IconCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("IconCell"));
        assertThat(cell.getIcon(), is("icon"));
        assertThat(cell.getPosition(), is(PositionEnum.RIGHT));

        cell = (IconCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getSrc(), is("IconCell"));
        assertThat(cell.getIcon(), is("`type.id == 1 ? 'icon1' : type.id == 2 ? 'icon2' : 'icon3'`"));
        assertThat(cell.getText(), is("text"));
        assertThat(cell.getPosition(), is(PositionEnum.LEFT));
        assertThat(cell.getTooltipFieldId(), is("tooltipId"));
    }
}

