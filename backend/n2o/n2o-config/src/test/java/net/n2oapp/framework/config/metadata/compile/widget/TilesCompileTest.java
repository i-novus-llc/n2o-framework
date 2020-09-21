package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oTextCell;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Компиляция виджета Плитки
 */
public class TilesCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oCellsIOPack());
    }

    @Test
    public void testTiles() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTilesCompile.page.xml")
                .get(new PageContext("testTilesCompile"));
        Tiles tiles = (Tiles) page.getWidgets().get("testTilesCompile_tiles1");

        assertThat(tiles.getSrc(), is("TilesWidget"));
        assertThat(tiles.getColsSm(), is(1));
        assertThat(tiles.getColsMd(), is(2));
        assertThat(tiles.getColsLg(), is(4));
        assertThat(tiles.getHeight(), is(450));
        assertThat(tiles.getWidth(), is(300));
        assertThat(tiles.getPaging().getSize(), is(10));

        Tiles.Tile tile = tiles.getTile().get(0);
        assertThat(tile.getId(), is("id1"));
        assertThat(tile.getClassName(), is("test1"));
        assertThat(tile.getStyle().size(), is(1));
        assertThat(tile.getStyle().get("style"), is("test1"));
        assertThat(tile.getSrc(), is("tile1"));
        assertThat(tile.getComponent().getSrc(), is("cell1"));

        tile = tiles.getTile().get(1);
        assertThat(tile.getId(), is("id2"));
        assertThat(tile.getSrc(), is("tile2"));
        assertThat(tile.getComponent(), instanceOf(N2oTextCell.class));
        assertThat(tile.getComponent().getSrc(), is("TextCell"));

        tile = tiles.getTile().get(2);
        assertThat(tile.getId(), is("id3"));
        assertThat(tile.getSrc(), is("ImageCell"));
        assertThat(tile.getComponent(), instanceOf(N2oImageCell.class));
        assertThat(((N2oImageCell) tile.getComponent()).getUrl(), is("/test"));

        tiles = (Tiles) page.getWidgets().get("testTilesCompile_tiles2");
        assertThat(tiles.getSrc(), is("TilesWidget"));
        assertThat(tiles.getTile().get(0).getSrc(), is("cell2"));
        assertThat(tiles.getColsSm(), is(2));
        assertThat(tiles.getColsMd(), is(3));
        assertThat(tiles.getColsLg(), is(5));
    }
}
