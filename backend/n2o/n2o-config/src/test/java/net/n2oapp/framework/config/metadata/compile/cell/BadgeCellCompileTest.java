package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.cell.BadgeCell;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.cell.v3.BadgeCellElementIOv3;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на компиляцию ячейки со значком
 */
class BadgeCellCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack());
        builder.ios(new BadgeCellElementIOv3());
        builder.compilers(new BadgeCellCompiler());
    }

    @Test
    void testBadgeCell() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/cell/testBadgeCell.page.xml")
                .get(new PageContext("testBadgeCell"));

        Table table = (Table) page.getWidget();
        BadgeCell cell = (BadgeCell) table.getComponent().getBody().getCells().get(0);
        assertThat(cell.getSrc(), is("BadgeCell"));
        assertThat(cell.getText(), is("text"));
        assertThat(cell.getFormat(), is("test"));
        assertThat(cell.getTextFormat(), is("test"));
        assertThat(cell.getColor(), is("`type.id == 1 ? 'success' : type.id == 2 ? 'danger' : 'info'`"));

        cell = (BadgeCell) table.getComponent().getBody().getCells().get(1);
        assertThat(cell.getColor(), is("info"));
        assertThat(cell.getShape(), is(ShapeTypeEnum.SQUARE));

        cell = (BadgeCell) table.getComponent().getBody().getCells().get(2);
        assertThat(cell.getShape(), is(ShapeTypeEnum.ROUNDED));
        assertThat(cell.getImageFieldId(), is("image"));
        assertThat(cell.getImagePosition(), is(PositionEnum.RIGHT));
        assertThat(cell.getImageShape(), is(ShapeTypeEnum.SQUARE));

        cell = (BadgeCell) table.getComponent().getBody().getCells().get(3);
        assertThat(cell.getShape(), is(ShapeTypeEnum.SQUARE));
        assertThat(cell.getImageFieldId(), is("img"));
        assertThat(cell.getImagePosition(), is(PositionEnum.LEFT));
        assertThat(cell.getImageShape(), is(ShapeTypeEnum.CIRCLE));
    }
}
