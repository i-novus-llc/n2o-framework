package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShowCountTypeEnum;
import net.n2oapp.framework.api.metadata.meta.cell.ImageCell;
import net.n2oapp.framework.api.metadata.meta.cell.TextCell;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции виджета Плитки
 */
class TilesWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oAllDataPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oCellsIOPack());
    }

    @Test
    void testTiles() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTilesCompile.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(new PageContext("testTilesCompile"));
        Tiles tiles = (Tiles) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(tiles.getSrc(), is("TilesWidget"));
        assertThat(tiles.getColsSm(), is(2));
        assertThat(tiles.getColsMd(), is(3));
        assertThat(tiles.getColsLg(), is(6));
        assertThat(tiles.getHeight(), is("450px"));
        assertThat(tiles.getWidth(), is("300px"));

        Tiles.Tile tile = tiles.getTile().get(0);
        assertThat(tile.getId(), is("test1"));
        assertThat(tile.getClassName(), is("test"));
        assertThat(tile.getStyle(), is(Map.of("color", "red")));
        assertThat(tile.getComponent().getSrc(), is("cell1"));

        tile = tiles.getTile().get(1);
        assertThat(tile.getId(), is("id2"));
        assertThat(tile.getComponent(), instanceOf(TextCell.class));
        assertThat(tile.getComponent().getSrc(), is("TextCell"));
        assertThat(tile.getComponent().getId(), is("id2"));
        assertThat(((TextCell) tile.getComponent()).getFieldKey(), is("test2"));
        assertThat(((TextCell) tile.getComponent()).getTooltipFieldId(), is("tooltip"));

        tile = tiles.getTile().get(2);
        assertThat(tile.getId(), is("test3"));
        assertThat(tile.getComponent(), instanceOf(ImageCell.class));
        assertThat(((ImageCell) tile.getComponent()).getData(), is("/test"));

        assertThat(tiles.getPaging().getNext(), is(true));
        assertThat(tiles.getPaging().getPrev(), is(true));
        assertThat(tiles.getPaging().getShowCount(), is(ShowCountTypeEnum.NEVER));
        assertThat(tiles.getPaging().getShowLast(), is(true));
        assertThat(tiles.getPaging().getSrc(), is("pagingSrc"));

        assertThat(page.getDatasources().get(tiles.getDatasource()).getPaging().getSize(), is(5));

        tiles = (Tiles) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(tiles.getSrc(), is("TilesWidget"));
        assertThat(tiles.getColsSm(), is(1));
        assertThat(tiles.getColsMd(), is(2));
        assertThat(tiles.getColsLg(), is(4));
        assertThat(tiles.getHeight(), nullValue());
        assertThat(tiles.getWidth(), nullValue());

        assertThat(tiles.getPaging().getNext(), is(false));
        assertThat(tiles.getPaging().getPrev(), is(false));
        assertThat(tiles.getPaging().getShowCount(), is(ShowCountTypeEnum.ALWAYS));
        assertThat(tiles.getPaging().getSize(), is(10));
    }
}
