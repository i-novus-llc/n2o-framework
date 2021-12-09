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

import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции виджета Плитки
 */
public class TilesWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oAllDataPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oCellsIOPack());
    }

    @Test
    public void testTiles() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTilesCompile.page.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml")
                .get(new PageContext("testTilesCompile"));
        Tiles tiles = (Tiles) page.getRegions().get("single").get(0).getContent().get(0);

        assertThat(tiles.getSrc(), is("TilesWidget"));
        assertThat(tiles.getColsSm(), is(2));
        assertThat(tiles.getColsMd(), is(3));
        assertThat(tiles.getColsLg(), is(6));
        assertThat(tiles.getHeight(), is(450));
        assertThat(tiles.getWidth(), is(300));

        Tiles.Tile tile = tiles.getTile().get(0);
        assertThat(tile.getId(), is("test1"));
        assertThat(tile.getClassName(), is("test"));
        assertThat(tile.getStyle(), is(Map.of("color", "red")));
        assertThat(tile.getComponent().getSrc(), is("cell1"));

        tile = tiles.getTile().get(1);
        assertThat(tile.getId(), is("id2"));
        assertThat(tile.getComponent(), instanceOf(N2oTextCell.class));
        assertThat(tile.getComponent().getSrc(), is("TextCell"));
        assertThat(tile.getComponent().getId(), is("id2"));
        assertThat(((N2oTextCell) tile.getComponent()).getFieldKey(), is("test2"));
        assertThat(((N2oTextCell) tile.getComponent()).getTooltipFieldId(), is("tooltip"));

        tile = tiles.getTile().get(2);
        assertThat(tile.getId(), is("test3"));
        assertThat(tile.getComponent(), instanceOf(N2oImageCell.class));
        assertThat(((N2oImageCell) tile.getComponent()).getData(), is("/test"));

        assertThat(tiles.getPaging().getFirst(), is(false));
        assertThat(tiles.getPaging().getShowSinglePage(), is(true));
        assertThat(tiles.getPaging().getLast(), is(true));
        assertThat(tiles.getPaging().getNext(), is(true));
        assertThat(tiles.getPaging().getPrev(), is(true));
        assertThat(tiles.getPaging().getShowCount(), is(false));
        assertThat(tiles.getPaging().getSize(), is(5));
        assertThat(tiles.getPaging().getSrc(), is("pagingSrc"));

        //fixme assertThat(page.getDatasources().get(tiles.getDatasource()).getProvider().getSize(), is(5));

        tiles = (Tiles) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(tiles.getSrc(), is("TilesWidget"));
        assertThat(tiles.getColsSm(), is(1));
        assertThat(tiles.getColsMd(), is(2));
        assertThat(tiles.getColsLg(), is(4));

        assertThat(tiles.getPaging().getFirst(), is(true));
        assertThat(tiles.getPaging().getShowSinglePage(), is(false));
        assertThat(tiles.getPaging().getLast(), is(false));
        assertThat(tiles.getPaging().getNext(), is(false));
        assertThat(tiles.getPaging().getPrev(), is(false));
        assertThat(tiles.getPaging().getShowCount(), is(true));
        assertThat(tiles.getPaging().getSize(), is(10));
    }
}
