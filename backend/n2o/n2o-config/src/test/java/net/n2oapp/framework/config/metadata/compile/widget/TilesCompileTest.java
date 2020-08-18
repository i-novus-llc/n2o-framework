package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.meta.widget.Tiles;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.TilesWidgetIOV4;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TilesCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack(), new N2oActionsPack())
                .ios(new TilesWidgetIOV4())
                .compilers(new TilesCompiler());
    }

    @Test
    public void testTiles() {
        Tiles tiles = (Tiles) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTilesCompile.widget.xml")
                .get(new WidgetContext("testTilesCompile"));
        assertThat(tiles.getId(), is("$testTilesCompile"));
        assertThat(tiles.getColsSm(), is(1));
        assertThat(tiles.getColsMd(), is(2));
        assertThat(tiles.getColsLg(), is(5));
//        assertThat(tiles.getSrc(), is("TilesWidget"));

        Tiles.Tile tile = tiles.getTile().get(0);
        assertThat(tile.getId(), is("id1"));
        assertThat(tile.getClassName(), is("test1"));
        assertThat(tile.getStyle(), is("test1"));
        assertThat(tile.getSrc(), is("tile1")); //todo
//        assertThat(tile.getComponent().getId(), is("cId1"));
        assertThat(tile.getComponent().getSrc(), is("cell1"));

        tile = tiles.getTile().get(1);
        assertThat(tile.getId(), is("id2"));
        assertThat(tile.getClassName(), is("test2"));
        assertThat(tile.getStyle(), is("test2"));
        assertThat(tile.getSrc(), is("tile2")); //todo
//        assertThat(tile.getComponent().getId(), is("cId2"));
        assertThat(tile.getComponent().getSrc(), is("cell2"));


//        assertThat(tiles.getDataProvider().getUrl(), is("n2o/data/testTilesCompile"));
    }
}
